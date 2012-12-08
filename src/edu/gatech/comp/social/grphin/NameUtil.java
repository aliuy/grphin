package edu.gatech.comp.social.grphin;

import java.util.HashMap;
import java.util.Map;

public class NameUtil {
  // Map of normalized names (Microsoft Corporation -> Microsoft)
  private static Map<String, String> normalizedNames;

  public static String normalize(String name) {
    if (normalizedNames == null) {
      normalizedNames = new HashMap<String, String>();
      normalizedNames.put("AbbottLaboratories", "Abbott");
      normalizedNames.put("ABNAMROBank", "ABNAMRO");
      normalizedNames.put("ABNAMROBankNV", "ABNAMRO");
      normalizedNames.put("AccentureInc", "Accenture");
      normalizedNames.put("AccentureServices", "Accenture");
      normalizedNames.put("AccentureTechnologySolutions", "Accenture");
      normalizedNames.put("AdeccoGroup", "Adecco");
      normalizedNames.put("AdobeSystems", "Adobe");
      normalizedNames.put("AgilityLogistics", "Agility");
      normalizedNames.put("AlcatelLucent", "Alcatel");
      normalizedNames.put("Amazoncom", "Amazon");
      normalizedNames.put("AmericanExpressBusinessTravel", "AmericanExpress");
      normalizedNames.put("AppleInc", "Apple");
      normalizedNames.put("AppleRetail", "Apple");
      normalizedNames.put("ArabBankPLC", "ArabBank");
      normalizedNames.put("ArabBankplc", "ArabBank");
      normalizedNames.put("AricentGroup", "Aricent");
      normalizedNames.put("AstraZenecaPharmaceuticals", "AstraZeneca");
      normalizedNames.put("AstraZenecaRD", "AstraZeneca");
      normalizedNames.put("AtosInternational", "Atos");
      normalizedNames.put("AtosOrigin", "Atos");
      normalizedNames.put("ATTMobility", "ATT");
      normalizedNames.put("BancoBradesco", "Bradesco");
      normalizedNames.put("BankofAmericaMerrillLynch", "MerrillLynch");
      normalizedNames.put("BarclaysBank", "Barclays");
      normalizedNames.put("BoehringerIngelheimPharmaceuticals", "BoehringerIngelheim");
      normalizedNames.put("BritishAmericanTobaccoNigeriaLimited", "BritishAmericanTobacco");
      normalizedNames.put("CapgeminiConsulting", "Capgemini");
      normalizedNames.put("CapgeminiIndiaPvtLtd", "Capgemini");
      normalizedNames.put("CiscoSystems", "Cisco");
      normalizedNames.put("CiscoSystemsInc", "Cisco");
      normalizedNames.put("Citibank", "Citi");
      normalizedNames.put("CitibankNA", "Citi");
      normalizedNames.put("Citigroup", "Citi");
      normalizedNames.put("CitiGroup", "Citi");
      normalizedNames.put("CognizantTechnologySolutions", "Cognizant");
      normalizedNames.put("ComputerSciencesCorporation", "CSC");
      normalizedNames.put("DaimlerChrysler", "Chrysler");
      normalizedNames.put("DanskeBankGroup", "DanskeBank");
      normalizedNames.put("DellInc", "Dell");
      normalizedNames.put("DellServices", "Dell");
      normalizedNames.put("DeloitteConsulting", "Deloitte");
      normalizedNames.put("DeloitteLLP", "Deloitte");
      normalizedNames.put("DeloitteTouche", "Deloitte");
      normalizedNames.put("DeloitteToucheLLP", "Deloitte");
      normalizedNames.put("DHLExpress", "DHL");
      normalizedNames.put("EDSanHPCompany", "EDS");
      normalizedNames.put("EDSanHPcompany", "EDS");
      normalizedNames.put("ElectronicDataSystems", "EDS");
      normalizedNames.put("EliLillyandCompany", "EliLilly");
      normalizedNames.put("EricssonAB", "Ericsson");
      normalizedNames.put("EricssonEgypt", "Ericsson");
      normalizedNames.put("EricssonMalaysia", "Ericsson");
      normalizedNames.put("EricssonNigeria", "Ericsson");
      normalizedNames.put("EtisalatNigeria", "Etisalat");
      normalizedNames.put("GECapital", "GE");
      normalizedNames.put("GEEnergy", "GE");
      normalizedNames.put("GEHealthcare", "GE");
      normalizedNames.put("GEMoney", "GE");
      normalizedNames.put("GeneralElectric", "GE");
      normalizedNames.put("GlaxoSmithKline", "GSK");
      normalizedNames.put("GlobacomLimited", "Globacom");
      normalizedNames.put("HCLTechnologiesLtd", "HCLTechnologies");
      normalizedNames.put("HewlettPackard", "HP");
      normalizedNames.put("HoffmannLaRoche", "Roche");
      normalizedNames.put("HoneywellAerospace", "Honeywell");
      normalizedNames.put("HPEnterpriseServices", "HP");
      normalizedNames.put("HPSoftware", "HP");
      normalizedNames.put("HSBCBank", "HSBC");
      normalizedNames.put("IBMArgentina", "IBM");
      normalizedNames.put("IBMAustralia", "IBM");
      normalizedNames.put("IBMBrasil", "IBM");
      normalizedNames.put("IBMBusinessConsultingServices", "IBM");
      normalizedNames.put("IBMCanadaLtd", "IBM");
      normalizedNames.put("IBMGlobalBusinessServices", "IBM");
      normalizedNames.put("IBMGlobalProcessServices", "IBM");
      normalizedNames.put("IBMGlobalServices", "IBM");
      normalizedNames.put("IBMIndia", "IBM");
      normalizedNames.put("IBMIndiaPvtLtd", "IBM");
      normalizedNames.put("IBMSoftwareGroup", "IBM");
      normalizedNames.put("IBMTivoliSoftware", "IBM");
      normalizedNames.put("ICICIBankLtd", "ICICIBank");
      normalizedNames.put("IndianaUniversityBloomington", "IndianaUniversity");
      normalizedNames.put("InfosysLimited", "Infosys");
      normalizedNames.put("InfosysTechnologiesLtd", "Infosys");
      normalizedNames.put("IntelCorporation", "Intel");
      normalizedNames.put("JPMorganChase", "JPMorgan");
      normalizedNames.put("KPITCumminsInfosystemsLimited", "KPITCummins");
      normalizedNames.put("KPMGAdvisory", "KPMG");
      normalizedNames.put("KPMGAudit", "KPMG");
      normalizedNames.put("KPMGLLP", "KPMG");
      normalizedNames.put("KPMGProfessionalServices", "KPMG");
      normalizedNames.put("LMEricssonNigeria", "Ericsson");
      normalizedNames.put("LogicaCMG", "Logica");
      normalizedNames.put("LOral", "LOreal");
      normalizedNames.put("MerckCoInc", "Merck");
      normalizedNames.put("MerckSharpDohme", "Merck");
      normalizedNames.put("MicrosoftCorporation", "Microsoft");
      normalizedNames.put("MotorolaInc", "Motorola");
      normalizedNames.put("MotorolaMobileDevices", "Motorola");
      normalizedNames.put("MotorolaMobility", "Motorola");
      normalizedNames.put("MotorolaSolutions", "Motorola");
      normalizedNames.put("MTNNigeria", "MTN");
      normalizedNames.put("NationalCommercialBank", "NCB");
      normalizedNames.put("NokiaMobilePhones", "Nokia");
      normalizedNames.put("NokiaNetworks", "NSN");
      normalizedNames.put("NokiaSiemensNetworks", "NSN");
      normalizedNames.put("NortelNetworks", "Nortel");
      normalizedNames.put("NovoNordiskAS", "NovoNordisk");
      normalizedNames.put("NykreditRealkreditAS", "Nykredit");
      normalizedNames.put("OracleCorporation", "Oracle");
      normalizedNames.put("PatniComputerSystemsLtd", "PatniComputerSystems");
      normalizedNames.put("PetroleumDevelopmentOman", "PDO");
      normalizedNames.put("PfizerInc", "Pfizer");
      normalizedNames.put("PfizerPharmaceuticals", "Pfizer");
      normalizedNames.put("PhilipsLighting", "Philips");
      normalizedNames.put("PricewaterhouseCoopers", "PwC");
      normalizedNames.put("PriceWaterhouseCoopers", "PwC");
      normalizedNames.put("ProcterGamble", "PG");
      normalizedNames.put("PTBankCIMBNiagaTbk", "PTBankCIMBNiaga");
      normalizedNames.put("PTTelkomsel", "Telkomsel");
      normalizedNames.put("PwCConsulting", "PwC");
      normalizedNames.put("RBCRoyalBank", "RBC");
      normalizedNames.put("RochePharmaceuticals", "Roche");
      normalizedNames.put("SafecoInsurance", "Safeco");
      normalizedNames.put("SAPLabs", "SAP");
      normalizedNames.put("SaudiTelecomCompany", "STC");
      normalizedNames.put("ScheringPloughPharmaceuticals", "ScheringPlough");
      normalizedNames.put("SchlumbergerInformationSolutions", "Schlumberger");
      normalizedNames.put("ShellOilCompany", "Shell");
      normalizedNames.put("SprintNextel", "Sprint");
      normalizedNames.put("StarbucksCoffeeCompany", "Starbucks");
      normalizedNames.put("STEricsson", "Ericsson");
      normalizedNames.put("TataConsultancyServices", "TCS");
      normalizedNames.put("TechMahindraLtd", "TechMahindra");
      normalizedNames.put("TescoStoresLtd", "Tesco");
      normalizedNames.put("TheAmericanUniversityinCairo", "AmericanUniversityinCairo");
      normalizedNames.put("TheHomeDepot", "HomeDepot");
      normalizedNames.put("TheUniversityofJordan", "UniversityofJordan");
      normalizedNames.put("ThomsonReuters", "Reuters");
      normalizedNames.put("TietoEnator", "Tieto");
      normalizedNames.put("TravelersInsurance", "Travelers");
      normalizedNames.put("TRWAutomotive", "TRW");
      normalizedNames.put("UBSInvestmentBank", "UBS");
      normalizedNames.put("UBSWealthManagement", "UBS");
      normalizedNames.put("UnileverHPC", "Unilever");
      normalizedNames.put("UnileverMashreq", "Unilever");
      normalizedNames.put("UnitedStatesAirForce", "USAF");
      normalizedNames.put("UniversityofGhanaLegon", "UniversityofGhana");
      normalizedNames.put("USAirForce", "USAF");
      normalizedNames.put("USNavyReserve", "USNavy");
      normalizedNames.put("VerizonBusiness", "Verizon");
      normalizedNames.put("VodafoneEgypt", "Vodafone");
      normalizedNames.put("VodafoneIT", "Vodafone");
      normalizedNames.put("WachoviaAWellsFargoCompany", "Wachovia");
      normalizedNames.put("WachoviaBank", "Wachovia");
      normalizedNames.put("WellsFargoBank", "WellsFargo");
      normalizedNames.put("WellsFargoFinancial", "WellsFargo");
      normalizedNames.put("WellsFargoHomeMortgage", "WellsFargo");
      normalizedNames.put("WesternUnionFinancialServices", "WesternUnion");
      normalizedNames.put("WiproInfotech", "Wipro");
      normalizedNames.put("WiproInfotechLtd", "Wipro");
      normalizedNames.put("WiproTechnologies", "Wipro");
      normalizedNames.put("WyethPharmaceuticals", "Wyeth");
      normalizedNames.put("XeroxCorporation", "Xerox");
      normalizedNames.put("ZainJordan", "Zain");
      normalizedNames.put("Zainjordan", "Zain");
      normalizedNames.put("ZainKSA", "Zain");
      normalizedNames.put("ZainNigeria", "Zain");
    }
    name = name.replaceAll("[^a-zA-Z0-9]", "");
    if (normalizedNames.containsKey(name)) {
      name = normalizedNames.get(name);
    }
    return name;
  }
}
