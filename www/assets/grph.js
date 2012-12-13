var sys;
  
var Renderer = function(canvas){
  var canvas = $(canvas).get(0);
  var ctx = canvas.getContext('2d');
  var particleSystem;
  var gfx = arbor.Graphics(canvas);
  var currNode;

  var intersect_line_line = function(p1, p2, p3, p4)
  {
    var denom = ((p4.y - p3.y)*(p2.x - p1.x) - (p4.x - p3.x)*(p2.y - p1.y));
    if (denom === 0) return false // lines are parallel
    var ua = ((p4.x - p3.x)*(p1.y - p3.y) - (p4.y - p3.y)*(p1.x - p3.x)) / denom;
    var ub = ((p2.x - p1.x)*(p1.y - p3.y) - (p2.y - p1.y)*(p1.x - p3.x)) / denom;

    if (ua < 0 || ua > 1 || ub < 0 || ub > 1)  return false
    return arbor.Point(p1.x + ua * (p2.x - p1.x), p1.y + ua * (p2.y - p1.y));
  };
    
  var intersect_line_box = function(p1, p2, boxTuple)
  {
    var p3 = {x:boxTuple[0], y:boxTuple[1]},
        w = boxTuple[2],
        h = boxTuple[3]
    
    var tl = {x: p3.x, y: p3.y};
    var tr = {x: p3.x + w, y: p3.y};
    var bl = {x: p3.x, y: p3.y + h};
    var br = {x: p3.x + w, y: p3.y + h};

    return intersect_line_line(p1, p2, tl, tr) ||
           intersect_line_line(p1, p2, tr, br) ||
           intersect_line_line(p1, p2, br, bl) ||
           intersect_line_line(p1, p2, bl, tl) ||
           false
  };
    
  var that = {
    init:function(system){
      particleSystem = system;
      particleSystem.screenSize(canvas.width, canvas.height) ;
      particleSystem.screenPadding(80); // leave an extra 80px of whitespace per side
      that.initMouseHandling();
    },
    
    redraw:function(){
      ctx.fillStyle = 'white';
      ctx.fillRect(0,0, canvas.width, canvas.height);
      ctx.font = "12px Helvetica";
      
      var nodeBoxes = {};
      particleSystem.eachNode(function(node, pt){
        // node: {mass:#, p:{x,y}, name:"", data:{}}
        // pt:   {x:#, y:#}  node position in screen coords
        
        // determine the box size and round off the coords if we'll be 
        // drawing a text label (awful alignment jitter otherwise...)
        var label = node.name||"";
        var w = ctx.measureText(""+label).width + 10;
        if (!(""+label).match(/^[ \t]*$/)){
          pt.x = Math.floor(pt.x);
          pt.y = Math.floor(pt.y);
        }else{
          label = null;
        }
        
        // draw the node
        ctx.fillStyle = "black";
        if(currNode && currNode.name == node.name) {
          ctx.fillStyle = "blue";
        }
        gfx.rect(pt.x-w/2, pt.y-10, w,20, 4, {fill:ctx.fillStyle});
        nodeBoxes[node.name] = [pt.x-w/2, pt.y-11, w, 22]

        // draw the text
        if (label){
          ctx.textAlign = "center";
          ctx.fillStyle = "white";
          if (node.data.color=='none') ctx.fillStyle = '#333333';
          ctx.fillText(label||"", pt.x, pt.y+4);
          ctx.fillText(label||"", pt.x, pt.y+4);
        }
      });

      particleSystem.eachEdge(function(edge, pt1, pt2){
        // edge: {source:Node, target:Node, length:#, data:{}}
        // pt1:  {x:#, y:#}  source position in screen coords
        // pt2:  {x:#, y:#}  target position in screen coords

        var weight = edge.data.size;
        var color = "black";
        
        if(currNode) {
          if(currNode.name == edge.source.name) {
            color = "red";
          }
          
          if(currNode.name == edge.target.name) {
            color = "green";
          }
        }

        // find the start point
        var tail = intersect_line_box(pt1, pt2, nodeBoxes[edge.source.name]);
        var head = intersect_line_box(tail, pt2, nodeBoxes[edge.target.name]);

        ctx.save() ;
        ctx.beginPath();

        ctx.lineWidth = 1;
        if (color) ctx.strokeStyle = color;
        // if (color) trace(color)
        ctx.fillStyle = null;

        ctx.moveTo(tail.x, tail.y);
        ctx.lineTo(head.x, head.y);
        ctx.stroke();
        ctx.restore();

        // draw an arrowpt2 if this is a -> style edge
        ctx.save()
        // move to the pt2 position of the edge we just drew
        var wt = 5;
        var arrowLength = 6 + wt;
        var arrowWidth = 2 + wt;
        ctx.fillStyle = (color) ? color : ctx.strokeStyle;
        ctx.translate(head.x, head.y);
        ctx.rotate(Math.atan2(head.y - tail.y, head.x - tail.x));

        // delete some of the edge that's already there (so the point isn't hidden)
        ctx.clearRect(-arrowLength/2,-wt/2, arrowLength/2,wt);

        // draw the chevron
        ctx.beginPath();
        ctx.moveTo(-arrowLength, arrowWidth);
        ctx.lineTo(0, 0);
        ctx.lineTo(-arrowLength, -arrowWidth);
        ctx.lineTo(-arrowLength * 0.8, -0);
        ctx.closePath();
        ctx.fill();
        ctx.restore()
      });
    },
    
    initMouseHandling:function(){
      // no-nonsense drag and drop (thanks springy.js)
      var dragged = null;

      // set up a handler object that will initially listen for mousedowns then
      // for moves and mouseups while dragging
      var handler = {
        clicked:function(e){
          var pos = $(canvas).offset();
          _mouseP = arbor.Point(e.pageX-pos.left, e.pageY-pos.top);
          dragged = particleSystem.nearest(_mouseP);

          if (dragged && dragged.node !== null){
            // while we're dragging, don't let physics move the node
            dragged.node.fixed = true;
            currNode = dragged.node;
            
            $('#panel').html(function() {
              var toRet = $('<div>').append($('<h1>').text(currNode.name).append($('<hr>')));
              if (currNode.data.size) {
                toRet.append($('<b>').text('# of LinkedIn Profiles: ' + currNode.data.size)).append('<br>').append('<br>');
              }
              toRet.append($('<b>').text('Employee Turnover:')).append('<br>');
              // Employee Turnover from getEdgesTo(currNode)
              $.each(sys.getEdgesTo(currNode), function(i, selectedEdge) {
                if (!selectedEdge.data.toSize || !selectedEdge.data.fromSize || selectedEdge.data.toSize != selectedEdge.data.fromSize) {
                  toRet.append(
                    $('<span>')
                      .text(selectedEdge.source.name + ': ')
                      .attr({style: 'color:green'})
                    );
                } else {
                  toRet.append(
                    $('<span>')
                      .text(selectedEdge.source.name + ': ')
                      .attr({style: 'color:black'})
                    );
                }
                if (selectedEdge.data.toSize) {
                  toRet.append(
                    $('<span>')
                      .text('+' + selectedEdge.data.toSize + ' ')
                      .attr({style: 'color:green'})
                  );
                }
                if (selectedEdge.data.fromSize) {
                  toRet.append(
                    $('<span>')
                      .text('-' + selectedEdge.data.fromSize + ' ')
                      .attr({style: 'color:red'})
                  );
                }
                toRet.append($('<br>'));
              });
              // Employee Turnover from getEdgesFrom(currNode)
              $.each(sys.getEdgesFrom(currNode), function(i, selectedEdge) {
                if (!selectedEdge.data.toSize || !selectedEdge.data.fromSize || selectedEdge.data.toSize != selectedEdge.data.fromSize) {
                  toRet.append(
                    $('<span>')
                      .text(selectedEdge.target.name + ': ')
                      .attr({style: 'color:red'})
                    );
                  if (selectedEdge.data.fromSize) {
                    toRet.append(
                      $('<span>')
                        .text('+' + selectedEdge.data.fromSize + ' ')
                        .attr({style: 'color:green'})
                    );
                  }
                  if (selectedEdge.data.toSize) {
                    toRet.append(
                      $('<span>')
                        .text('-' + selectedEdge.data.toSize + ' ')
                        .attr({style: 'color:red'})
                    );
                  }
                  toRet.append($('<br>'));
                }
              });
              
              return toRet;
            });
          }

          $(canvas).bind('mousemove', handler.dragged);
          $(window).bind('mouseup', handler.dropped);

          return false
        },
        dragged:function(e){
          var pos = $(canvas).offset();
          var s = arbor.Point(e.pageX-pos.left, e.pageY-pos.top);

          if (dragged && dragged.node !== null){
            var p = particleSystem.fromScreen(s);
            dragged.node.p = p;
          }

          return false;
        },

        dropped:function(e){
          if (dragged===null || dragged.node===undefined) return;
          if (dragged.node !== null) dragged.node.fixed = false;
          dragged.node.tempMass = 1000;
          dragged = null;
          $(canvas).unbind('mousemove', handler.dragged);
          $(window).unbind('mouseup', handler.dropped);
          _mouseP = null;
          return false;
        }
      }
      
      // start listening
      $(canvas).mousedown(handler.clicked);

    },
    
  }
  return that;
}    

$(document).ready(function(){
  var canvas = $('#viewport')[0];
  canvas.width = $('#content')[0].clientWidth*0.85;
  canvas.height = $('#content')[0].clientHeight;

  $(window).resize(function() {
    var canvas = $('#viewport')[0];
    canvas.width = $('#content')[0].clientWidth*0.85;
    canvas.height = $('#content')[0].clientHeight;
  });

  // create the system with sensible repulsion/stiffness/friction
  sys = arbor.ParticleSystem(2000, 600, 0.8);
  // use center-gravity to make the graph settle nicely (ymmv)
  sys.parameters({gravity:true});
  // our newly created renderer will have its .init() method called shortly by sys...
  sys.renderer = Renderer('#viewport'); 
  
  $.ajax({
    type: 'GET',
    url: 'assets/jsonOutput.json',
    success: function(data){
      $.each(data.nodes, function(i, v){
        sys.addNode(v.name, {size:v.size});
      });
      $.each(data.edges, function(i, v){
        sys.addEdge(v.source, v.destination, {toSize:v.toSize, fromSize:v.fromSize});
      });
    },
    dataType: 'JSON'
  });
});

