package edu.gatech.comp.social.grphin;

import java.util.HashMap;
import java.util.Map;

public class NameUtil {
  // Map of normalized names (Microsoft Corporation -> Microsoft)
  private static Map<String, String> normalizedNames;

  public static String normalize(String name) {
    if (normalizedNames == null) {
      normalizedNames = new HashMap<String, String>();
      normalizedNames.put("ABNAMROBank", "ABNAMRO");
      normalizedNames.put("ABNAMROBankNV", "ABNAMRO");
      normalizedNames.put("AccentureInc", "Accenture");
      normalizedNames.put("AccentureTechnologySolutions", "Accenture");
      normalizedNames.put("AdeccoGroup", "Adecco");
      normalizedNames.put("AdobeSystems", "Adobe");
      normalizedNames.put("AgilityLogistics", "Agility");
      normalizedNames.put("AlcatelLucent", "Alcatel");
      normalizedNames.put("AmericanExpressBusinessTravel", "AmericanExpress");
      normalizedNames.put("AppleInc", "Apple");
      normalizedNames.put("AppleRetail", "Apple");
      normalizedNames.put("ArabBankPLC", "ArabBank");
      normalizedNames.put("ArabBankplc", "ArabBank");
      normalizedNames.put("AricentGroup", "Aricent");
      normalizedNames.put("AstraZenecaPharmaceuticals", "AstraZeneca");
      normalizedNames.put("AstraZenecaRD", "AstraZeneca");
      normalizedNames.put("ATTMobility", "ATT");
      normalizedNames.put("BancoBradesco", "Bradesco");
      normalizedNames.put("BarclaysBank", "Barclays");
      normalizedNames.put("BritishAmericanTobaccoNigeriaLimited", "BritishAmericanTobacco");
      normalizedNames.put("CapgeminiConsulting", "Capgemini");
      normalizedNames.put("CapgeminiIndiaPvtLtd", "Capgemini");
      normalizedNames.put("CiscoSystems", "Cisco");
      normalizedNames.put("CiscoSystemsInc", "Cisco");
      normalizedNames.put("Citibank", "Citi");
      normalizedNames.put("CitibankNA", "Citi");
      normalizedNames.put("Citigroup", "Citi");
      normalizedNames.put("CognizantTechnologySolutions", "Cognizant");
      normalizedNames.put("DanskeBankGroup", "DanskeBank");
      normalizedNames.put("DellInc", "Dell");
      normalizedNames.put("DeloitteConsulting", "Deloitte");
      normalizedNames.put("DeloitteLLP", "Deloitte");
      normalizedNames.put("DeloitteTouche", "Deloitte");
      normalizedNames.put("DeloitteToucheLLP", "Deloitte");
      normalizedNames.put("DHLExpress", "DHL");
      normalizedNames.put("EricssonEgypt", "Ericsson");
      normalizedNames.put("EricssonMalaysia", "Ericsson");
      normalizedNames.put("EricssonNigeria", "Ericsson");
      normalizedNames.put("EtisalatNigeria", "Etisalat");
      normalizedNames.put("GECapital", "GE");
      normalizedNames.put("GEEnergy", "GE");
      normalizedNames.put("GeneralElectric", "GE");
      normalizedNames.put("GlobacomLimited", "Globacom");
      normalizedNames.put("HCLTechnologiesLtd", "HCLTechnologies");
      normalizedNames.put("HewlettPackard", "HP");
      normalizedNames.put("HoffmannLaRoche", "Roche");
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
      normalizedNames.put("MicrosoftCorporation", "Microsoft");
      normalizedNames.put("MotorolaMobility", "Motorola");
      normalizedNames.put("MotorolaSolutions", "Motorola");
      normalizedNames.put("MTNNigeria", "MTN");
      normalizedNames.put("NortelNetworks", "Nortel");
      normalizedNames.put("NovoNordiskAS", "NovoNordisk");
      normalizedNames.put("NykreditRealkreditAS", "Nykredit");
      normalizedNames.put("OracleCorporation", "Oracle");
      normalizedNames.put("PatniComputerSystemsLtd", "PatniComputerSystems");
      normalizedNames.put("PfizerInc", "Pfizer");
      normalizedNames.put("PfizerPharmaceuticals", "Pfizer");
      normalizedNames.put("PricewaterhouseCoopers", "PwC");
      normalizedNames.put("PriceWaterhouseCoopers", "PwC");
      normalizedNames.put("ProcterGamble", "PG");
      normalizedNames.put("PTBankCIMBNiagaTbk", "PTBankCIMBNiaga");
      normalizedNames.put("PTTelkomsel", "Telkomsel");
      normalizedNames.put("PwCConsulting", "PwC");
      normalizedNames.put("RochePharmaceuticals", "Roche");
      normalizedNames.put("SafecoInsurance", "Safeco");
      normalizedNames.put("SAPLabs", "SAP");
      normalizedNames.put("SchlumbergerInformationSolutions", "Schlumberger");
      normalizedNames.put("SprintNextel", "Sprint");
      normalizedNames.put("StarbucksCoffeeCompany", "Starbucks");
      normalizedNames.put("TataConsultancyServices", "TCS");
      normalizedNames.put("TechMahindraLtd", "TechMahindra");
      normalizedNames.put("TescoStoresLtd", "Tesco");
      normalizedNames.put("TheAmericanUniversityinCairo", "AmericanUniversityinCairo");
      normalizedNames.put("TheHomeDepot", "HomeDepot");
      normalizedNames.put("TheUniversityofJordan", "UniversityofJordan");
      normalizedNames.put("TravelersInsurance", "Travelers");
      normalizedNames.put("TRWAutomotive", "TRW");
      normalizedNames.put("UBSInvestmentBank", "UBS");
      normalizedNames.put("UnileverMashreq", "Unilever");
      normalizedNames.put("UniversityofGhanaLegon", "UniversityofGhana");
      normalizedNames.put("USNavyReserve", "USNavy");
      normalizedNames.put("VerizonBusiness", "Verizon");
      normalizedNames.put("VodafoneEgypt", "Vodafone");
      normalizedNames.put("VodafoneIT", "Vodafone");
      normalizedNames.put("WachoviaAWellsFargoCompany", "WellsFargo");
      normalizedNames.put("WellsFargoBank", "WellsFargo");
      normalizedNames.put("WellsFargoHomeMortgage", "WellsFargo");
      normalizedNames.put("WesternUnionFinancialServices", "WesternUnion");
      normalizedNames.put("WyethPharmaceuticals", "Wyeth");
      normalizedNames.put("ZainJordan", "Zain");
      normalizedNames.put("Zainjordan", "Zain");
      normalizedNames.put("ZainNigeria", "Zain");

    }
    name = name.replaceAll("[^a-zA-Z0-9]", "");
    if (normalizedNames.containsKey(name)) {
      name = normalizedNames.get(name);
    }
    return name;
  }
}
