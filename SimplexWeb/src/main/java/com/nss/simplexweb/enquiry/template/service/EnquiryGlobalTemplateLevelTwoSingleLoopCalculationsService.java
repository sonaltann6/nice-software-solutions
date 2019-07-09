package com.nss.simplexweb.enquiry.template.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nss.simplexweb.enquiry.template.model.EnquiryTemplateBean;
import com.nss.simplexweb.enquiry.template.model.fibcCosting.FibcCostTable;
import com.nss.simplexweb.enquiry.template.service.fibcCosting.FibcCostTableService;
import com.nss.simplexweb.enums.ENQUIRY;
import com.nss.simplexweb.enums.ROLE;
import com.nss.simplexweb.user.model.User;

@Service("enquiryGlobalTemplateLevelTwoSingleLoopCalculationsService")
public class EnquiryGlobalTemplateLevelTwoSingleLoopCalculationsService {

	@Autowired
	private FibcCostTableService fibcCostTableService;

	public EnquiryTemplateBean calculateBagWeight(EnquiryTemplateBean enquiryTemplateBean) {
		Double bagWeight = 0.0, bagTube = 0.0, bagBase = 0.0, bagInletAttachment = 0.0, bagOutletAttachment = 0.0,
				bagThread = 0.0, bagLiner = 0.0, bagFillerCord = 0.0, bagPerimeterBand = 0.0, bagBellyBand = 0.0, bagSteevdoorStrapBand = 0.0, bagBaffelGSM = 0.0, bagLinerGlued = 0.0, bagLoopProtector = 0.0;
		Double inletLength = 0.0, outletLength = 0.0, outletDimeter = 0.0, inletDiameter = 0.0;
		int bagDoc = 0, bagLabel = 0, bagTieForTop = 0, bagTieForBottom = 0;
		// Adding Loop Cover
		Double loopCover = 20.0; // in gm
		
		
		// 1. bag tube
		bagTube = calculateBagTube(enquiryTemplateBean.getSurfaceLength(), enquiryTemplateBean.getSurfaceWidth(),
				enquiryTemplateBean.getSurfaceHeight(), enquiryTemplateBean.getFabricGSMValue(),
				enquiryTemplateBean.getLoopHeight());

		// 2. bag base
		bagBase = calculateBagBase(enquiryTemplateBean.getSurfaceLength(), enquiryTemplateBean.getSurfaceWidth(),
				enquiryTemplateBean.getFabricGSMValue());

		// 3. inlet / top filling
		if (enquiryTemplateBean.isTopFilling()) {
			// a. top skirt
			if (enquiryTemplateBean.getTopType().getTopTypeAbbr().equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_SKIRT)) {
				bagInletAttachment = calculateAttachmentTopSkirt(enquiryTemplateBean.getSurfaceLength(),
						enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getTopSkirtLength(),
						enquiryTemplateBean.getTopSkirtGSM());
			}
			// b. top spout
			else if (enquiryTemplateBean.getTopType().getTopTypeAbbr().equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_SPOUT)) {
				bagInletAttachment = calculateAttachmentTopSpout(enquiryTemplateBean.getSurfaceLength(),
						enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getTopSpoutLength(),
						enquiryTemplateBean.getTopSpoutGSM(), enquiryTemplateBean.getTopSpoutDiameter());
				bagInletAttachment = bagInletAttachment
						+ calculateAttachmentTopFabric(enquiryTemplateBean.getSurfaceLength(),
								enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getTopSpoutGSM());
			}
			// c. top flap
			else if (enquiryTemplateBean.getTopType().getTopTypeAbbr().equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_FLAP)) {
				bagInletAttachment = calculateAttachmentTopFlap(enquiryTemplateBean.getSurfaceLength(),
						enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getTopFlapGSM());
			}
			// d. top skirt with flap
			else if (enquiryTemplateBean.getTopType().getTopTypeAbbr()
					.equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_SKIRT_WITH_FLAP)) {
				bagInletAttachment = calculateAttachmentTopSkirt(enquiryTemplateBean.getSurfaceLength(),
						enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getTopSkirtLength(),
						enquiryTemplateBean.getTopSkirtGSM());
				bagInletAttachment = bagInletAttachment
						+ calculateAttachmentTopFlap(enquiryTemplateBean.getSurfaceLength(),
								enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getTopFlapGSM());
			} // e. Top Spout with Flap
			else if (enquiryTemplateBean.getTopType().getTopTypeAbbr()
					.equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_SPOUT_WITH_FLAP)) {
				bagInletAttachment = calculateAttachmentTopSpout(enquiryTemplateBean.getSurfaceLength(),
						enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getTopSpoutLength(),
						enquiryTemplateBean.getTopSpoutGSM(), enquiryTemplateBean.getTopSpoutDiameter());
				bagInletAttachment = bagInletAttachment
						+ calculateAttachmentTopFabric(enquiryTemplateBean.getSurfaceLength(),
								enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getTopSpoutGSM());
				bagInletAttachment = bagInletAttachment
						+ calculateAttachmentTopFlap(enquiryTemplateBean.getSurfaceLength(),
								enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getTopFlapGSM());
			}
			// f. Conical Top
			else if (enquiryTemplateBean.getTopType().getTopTypeAbbr().equalsIgnoreCase(ENQUIRY.TOP_TYPE_CONICAL_TOP)) {
				bagInletAttachment = calculateAttachmentConicalTop(enquiryTemplateBean.getSurfaceLength(),
						enquiryTemplateBean.getSurfaceWidth());
			}
			// g. Platted Top
			else if (enquiryTemplateBean.getTopType().getTopTypeAbbr().equalsIgnoreCase(ENQUIRY.TOP_TYPE_PLEATED_TOP)) {
				bagInletAttachment = calculateAttachmentPlattedTop(enquiryTemplateBean.getSurfaceLength(),
						enquiryTemplateBean.getSurfaceWidth());
			}
			// h. Conical with Top Flap
			else if (enquiryTemplateBean.getTopType().getTopTypeAbbr()
					.equalsIgnoreCase(ENQUIRY.TOP_TYPE_CONICAL_TOP_WITH_FLAP)) {
				bagInletAttachment = calculateAttachmentConicalTop(enquiryTemplateBean.getSurfaceLength(),
						enquiryTemplateBean.getSurfaceWidth());
				bagInletAttachment = bagInletAttachment
						+ calculateAttachmentTopFlap(enquiryTemplateBean.getSurfaceLength(),
								enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getTopFlapGSM());
			}
			// e. Platted with Top Flap
			else if (enquiryTemplateBean.getTopType().getTopTypeAbbr()
					.equalsIgnoreCase(ENQUIRY.TOP_TYPE_PLEATED_TOP_WITH_FLAP)) {
				bagInletAttachment = calculateAttachmentPlattedTop(enquiryTemplateBean.getSurfaceLength(),
						enquiryTemplateBean.getSurfaceWidth());
				bagInletAttachment = bagInletAttachment
						+ calculateAttachmentTopFlap(enquiryTemplateBean.getSurfaceLength(),
								enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getTopFlapGSM());
			}
		}
		
			// 4. Outlet/ If bottom discharge - Yes
			if (enquiryTemplateBean.isBottomDischarge()) {
				bagOutletAttachment = calculateAttachmentOutletBottomSpout(enquiryTemplateBean.getBottomSpoutDiameter(),
						enquiryTemplateBean.getBottomSpoutGSM(), enquiryTemplateBean.getBottomSpoutLength(),
						enquiryTemplateBean.getSurfaceLength(), enquiryTemplateBean.getSurfaceWidth(),
						enquiryTemplateBean.getSurfaceHeight(),
						enquiryTemplateBean.getBottomType().getBottomTypeAbbr());
			}

		// 5. bag thread
		if (!enquiryTemplateBean.isTopFilling() && !enquiryTemplateBean.isBottomDischarge()) {
			// calculate thread without top & bottom
			bagThread = calculateThreadWithoutTopBottom(enquiryTemplateBean.getSurfaceLength(),
					enquiryTemplateBean.getSurfaceWidth());
		} else {
			// calculate thread with top & bottom
			if (enquiryTemplateBean.isTopFilling()) {
				if (enquiryTemplateBean.getTopType().getTopTypeAbbr().equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_SKIRT)
						|| enquiryTemplateBean.getTopType().getTopTypeAbbr()
								.equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_SKIRT_WITH_FLAP)) {
					inletLength = enquiryTemplateBean.getTopSkirtLength();
				} else if (enquiryTemplateBean.getTopType().getTopTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_SPOUT)
						|| enquiryTemplateBean.getTopType().getTopTypeAbbr()
								.equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_SPOUT_WITH_FLAP)) {
					inletLength = enquiryTemplateBean.getTopSpoutLength();
					inletDiameter = enquiryTemplateBean.getTopSpoutDiameter();
				}
			}
			if (enquiryTemplateBean.isBottomDischarge()) {
				outletLength = enquiryTemplateBean.getBottomSpoutLength();
				outletDimeter = enquiryTemplateBean.getBottomSpoutDiameter();
			}
			// If top filling is - Yes
			if (enquiryTemplateBean.isTopFilling()) {
				if (enquiryTemplateBean.getTopType().getTopTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_SKIRT_WITH_FLAP)
						|| enquiryTemplateBean.getTopType().getTopTypeAbbr()
								.equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_SPOUT_WITH_FLAP)
						|| enquiryTemplateBean.getTopType().getTopTypeAbbr()
								.equalsIgnoreCase(ENQUIRY.TOP_TYPE_CONICAL_TOP_WITH_FLAP)
						|| enquiryTemplateBean.getTopType().getTopTypeAbbr()
								.equalsIgnoreCase(ENQUIRY.TOP_TYPE_PLEATED_TOP_WITH_FLAP)) {

					bagThread = calculateThreadForBag(enquiryTemplateBean.getSurfaceLength(),
							enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getSurfaceHeight(),
							enquiryTemplateBean.isTopFilling(),
							enquiryTemplateBean.getTopFillingType().getTopFillingTypeAbbr(), inletLength, inletDiameter,
							enquiryTemplateBean.isBottomDischarge(), outletLength, outletDimeter);
					bagThread = bagThread + calculateThreadForBag(enquiryTemplateBean.getSurfaceLength(),
							enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getSurfaceHeight(),
							enquiryTemplateBean.isTopFilling(),
							enquiryTemplateBean.getTopFillingType().getTopFillingTypeAbbr(), 0.0, 0.0,
							enquiryTemplateBean.isBottomDischarge(), outletLength, outletDimeter);
				} 
			}
			// If top filling is - No
			else {
				bagThread = calculateThreadForBag(enquiryTemplateBean.getSurfaceLength(),
						enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getSurfaceHeight(),
						enquiryTemplateBean.isTopFilling(),
						enquiryTemplateBean.getTopFillingType().getTopFillingTypeAbbr(), inletLength, inletDiameter,
						enquiryTemplateBean.isBottomDischarge(), outletLength, outletDimeter);
			}
		}
		System.out.println("bag thread: " + bagThread);

		//6. liner
		// Liner tube value
		if (enquiryTemplateBean.getLinerTubeValue() <= 0) {
			enquiryTemplateBean.setLinerTubeValue(calculateLinerTube(enquiryTemplateBean.getSurfaceLength(), enquiryTemplateBean.getSurfaceWidth()));
		}

		//liner height
		double topLen = 0, bottomLen = 0;
		if (enquiryTemplateBean.getLinerHeightValue() <= 0) {
			if (enquiryTemplateBean.isTopFilling()) {
				if (enquiryTemplateBean.getTopFillingType().getTopFillingTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_SKIRT)
						|| enquiryTemplateBean.getTopFillingType().getTopFillingTypeAbbr()
								.equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_SKIRT_WITH_FLAP)) {
					topLen = enquiryTemplateBean.getTopSkirtLength();

				} else if ((enquiryTemplateBean.getTopFillingType().getTopFillingTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_SPOUT)
						|| enquiryTemplateBean.getTopFillingType().getTopFillingTypeAbbr()
								.equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_SPOUT_WITH_FLAP))) {
					topLen = enquiryTemplateBean.getTopSpoutLength();
				}
			}

			if (enquiryTemplateBean.isBottomDischarge()) {
				bottomLen = enquiryTemplateBean.getBottomSpoutLength();
			}
			enquiryTemplateBean.setLinerHeightValue(calculateLinerHeight(enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getSurfaceHeight(),
							topLen, bottomLen, enquiryTemplateBean.getTopFillingType().getTopFillingTypeAbbr(),
							enquiryTemplateBean.getLinerType().getLinerTypeAbbr()));
		}

		// If Liner
		if (enquiryTemplateBean.isLiner()) {
			bagLiner = calculateLiner(enquiryTemplateBean.getLinerMicronValue(),
					enquiryTemplateBean.getLinerTubeValue(), enquiryTemplateBean.getLinerHeightValue());
		}
		
		//7.
		bagFillerCord = calculateFillerCord(bagThread,
				enquiryTemplateBean.getFabricBagSeamType().getBagseamTypeAbbr());
		bagTieForTop = tieForTop(enquiryTemplateBean);
		System.out.println("tie for top: "+bagTieForTop);
		bagTieForBottom = tieForBottom(enquiryTemplateBean);
		System.out.println("tie for bottome: "+bagTieForBottom);
		bagDoc = totalDocument((enquiryTemplateBean.isDocPouch()) ? enquiryTemplateBean.getDocPouchValue() : 0);
		bagLabel = totalLabel((enquiryTemplateBean.isOtherLabels()) ? 1 : 0);
		
		//8. perimeter band
		if(enquiryTemplateBean.isPerimeterBand()) {
			bagPerimeterBand = calculatePerimeterBand(enquiryTemplateBean.getSurfaceLength(),
					enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getSwl());
		}
		
		//9. belly band
		if (enquiryTemplateBean.isBellyBand()) {
			bagBellyBand = calculateBellyBand(enquiryTemplateBean.getSurfaceLength(),
					enquiryTemplateBean.getSurfaceWidth());
		}
		
		//10. steev door strap
		if (enquiryTemplateBean.isSteevdoorStrap()) {
			bagSteevdoorStrapBand = calculateSteevdoorStrap(enquiryTemplateBean.getSurfaceLength(),
					enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getSwl(),
					enquiryTemplateBean.getProductSFtype().getSfTypeValue());
		}
		
		//11. baffle weight
		if (enquiryTemplateBean.isBaffle()) {
			bagBaffelGSM = calculateBaffleWeight(enquiryTemplateBean.getSurfaceLength(),
					enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getSurfaceHeight(),
					enquiryTemplateBean.getBaffleValue());
		}
		
		//12. loop protector
		if (enquiryTemplateBean.isLoopProtector()) {
			bagLoopProtector = calculateLoopProtector(enquiryTemplateBean.getLoopHeight(),
					enquiryTemplateBean.getLoopProtectorValue(), enquiryTemplateBean.getLoopNumber());
		}
		
		//13. liner glued
		if (enquiryTemplateBean.isLiner()) {
			if (enquiryTemplateBean.isLinerGlued()) {
				bagLinerGlued = 100.0;
			}
		}
		
		bagWeight = (bagTube + loopCover + bagBase + bagInletAttachment + bagOutletAttachment + bagThread  + bagLiner + bagFillerCord + bagTieForTop + bagTieForBottom + bagDoc + bagLabel + bagPerimeterBand + bagBellyBand + bagSteevdoorStrapBand + bagBaffelGSM + bagLinerGlued + bagLoopProtector) / 1000;
		System.out.println("bag weight: "+bagWeight);
		enquiryTemplateBean.setTotalBagWeight(bagWeight);
		return enquiryTemplateBean;
	}

	public EnquiryTemplateBean calculateBagCost(EnquiryTemplateBean enquiryTemplateBean) {
		int fabricColorCount = 0, printingSide = 0, linerTabbingCount = 0, docCount = 0, bagCount = 0, labelCount = 0,
				topBlockCount = 0, bottomBlockCount = 0, topVelcroCount = 0, bottomVelcroCount = 0;

		if (enquiryTemplateBean.getShipmentQuantity() != null) {
			bagCount = enquiryTemplateBean.getShipmentQuantity().intValue();
			System.out.println("bag count " +bagCount);
		}
		if (enquiryTemplateBean.getFabricColor().getFabricColorName() != null
				&& !enquiryTemplateBean.getFabricColor().getFabricColorName().equals("")) {
			fabricColorCount = enquiryTemplateBean.getFabricColor().getFabricColorId().intValue();
			System.out.println("fabricColorCount " +fabricColorCount);
		}
		if (enquiryTemplateBean.isPrinting()) {
			printingSide = (enquiryTemplateBean.getSides().getSidesId()).intValue();
			System.out.println("printingSide " +printingSide);
		}
		if (enquiryTemplateBean.isLinerTabbing()) {
			linerTabbingCount = enquiryTemplateBean.getLinerTabbingNumber().intValue();
			System.out.println("linerTabbingValue " +linerTabbingCount);
		}
		if (enquiryTemplateBean.isDocPouch()) {
			docCount = enquiryTemplateBean.getDocPouchValue().intValue();
			System.out.println("docNumber " +docCount);
		}
		if (enquiryTemplateBean.isOtherLabels()) {
			labelCount = Integer.parseInt(enquiryTemplateBean.getOtherLabelsValue());
			System.out.println("labelCountStr " +labelCount);
		}
		if (enquiryTemplateBean.isTopBlock()) {
			topBlockCount = enquiryTemplateBean.getTopBlockNumber().intValue();
			System.out.println("topBLockValue " +topBlockCount);
		}
		if (enquiryTemplateBean.isBottomBlock()) {
			bottomBlockCount = enquiryTemplateBean.getBottomBlockNumber().intValue();
			System.out.println("bottomBLockValue " +bottomBlockCount);
		}
		if (enquiryTemplateBean.isTopVelcroTie()) {
			topVelcroCount = enquiryTemplateBean.getTopVelcroTieNumber().intValue();
			System.out.println("topVelcroTypeValue " +topVelcroCount); 
		}
		if (enquiryTemplateBean.isBottomVelcroTie()) {
			bottomVelcroCount = enquiryTemplateBean.getBottomVelcroTieNumber().intValue();
			System.out.println("bottomVelcroTieValue " +bottomVelcroCount);
		}

		double bagDBRWCost = 0.0, bagDBCCCost = 0.0, uvDBCost = 0.0, unDBCost = 0.0, palletDBCost = 0.0,
				fobDBCost = 0.0, cifDBCost = 0.0, printDBCost = 0.0;
		double linerDBRWCost = 0.0, linerDBCCCost = 0.0, linerDBPalletCost = 0.0, linerDBfobCost = 0.0,
				linerDBcifCost = 0.0;

		double attachmentTopDBCost = 0, attachmentBottomDBCost = 0, colorFabricDBCost = 0, baffelDBCost = 0,
				singleFillerDBCost = 0, doubleFillerDBCost = 0, trippleFillerDBCost = 0;
		double conicalTopDBCost = 0, conicalBottomDBCost = 0, verLoopDBCost = 0, bLockDBCost = 0, zipLockDBCost = 0;
		double ventilatedFabricDBCost = 0, sulzerFabricDBCost = 0, raschelFabricDBCost = 0, lenoFabricDBCost = 0,
				condcutiveFabricDBCost = 0, bTypeFabricDBCost = 0;
		double linerTabbingDBCost = 0, linerGluingDBCost = 0, preShapeLinerDBCost = 0, bottolShapeLinerDBCost = 0,
				formFitLinerDBCost = 0, transperentFabricLinerDBCost = 0, flangesLinerDBCost = 0;
		double multifilamentDBCost = 0, fabirilatedLoopDBCost = 0, ppLoopDBCost = 0, polysterDBCost = 0,
				labelDBCost = 0, otherLabelDBCost = 0;

		double bagPrintCost = 0,
				bagFabricTypeCost = 0;
		double topBlockCost = 0, bottomBlockCost = 0, topVelcroCost = 0, bottomVelcroCost = 0, bagDocTypeCost = 0,
				bagLinerTabbingCost = 0, bagLinerGluingCost = 0, bagLinerTypeCost = 0;
		double bagUNCost = 0, bagLabelCost = 0, bagOtherLabelCost = 0,
				extraCleaningDBCost = 0, metalDetectorDBCost = 0, velcroTieCost = 0;

		String item_id = null;
		double item_cost = 0.0;
		double totalCost = 0.0;
		boolean result = false;
		double bagWeight = enquiryTemplateBean.getTotalBagWeight();

		User currentUser = enquiryTemplateBean.getRequester();
		List<FibcCostTable> costTable = fibcCostTableService.findAllByUserId(currentUser.getUserId());
		
		for(FibcCostTable ct : costTable) {
			System.out.println(ct.toString());
		}

		// calculating bag cost on the basis of item cost and item id.
		if (bagWeight > 0) {
			result = true;
			//Iterator<FibcCostTable> costTableIterator = costTable.iterator();
			//while (costTableIterator.hasNext()) {
			for(FibcCostTable costTableIterator : costTable) {
				try {
					
					item_id = costTableIterator.getItemId();
					item_cost = costTableIterator.getItemCost();
					System.out.println(item_id  +" " +item_cost);
					switch (item_id) {
					case "itemId-1":
						bagDBRWCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+bagDBRWCost );
						break;
					case "itemId-2":
						bagDBCCCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+bagDBCCCost );
						break;
					case "itemId-3":
						uvDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+uvDBCost );
						break;
					case "itemId-4":
						fobDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+fobDBCost );
						break;
					case "itemId-5":
						palletDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+palletDBCost );
						break;
					case "itemId-6":
						cifDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+cifDBCost );
						break;
					case "itemId-7":
						attachmentTopDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+attachmentTopDBCost );
						break;
					case "itemId-8":
						attachmentBottomDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+attachmentBottomDBCost );
						break;
					case "itemId-9":
						colorFabricDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+colorFabricDBCost );
						break;
					case "itemId-10":
						baffelDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+baffelDBCost );
						break;
					case "itemId-11":
						singleFillerDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+singleFillerDBCost );
						break;
					case "itemId-12":
						doubleFillerDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+doubleFillerDBCost );
						break;
					case "itemId-13":
						trippleFillerDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+trippleFillerDBCost );
						break;
					case "itemId-14":
						conicalTopDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+conicalTopDBCost );
						break;
					case "itemId-15":
						conicalBottomDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+conicalBottomDBCost );
						break;
					case "itemId-16":
						verLoopDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+verLoopDBCost );
						break;
					case "itemId-17":
						printDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+printDBCost );
						break;
					case "itemId-18":
						bLockDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+bLockDBCost );
						break;
					case "itemId-19":
						zipLockDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+zipLockDBCost );
						break;
					case "itemId-20":
						ventilatedFabricDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+ventilatedFabricDBCost );
						break;
					case "itemId-21":
						sulzerFabricDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+sulzerFabricDBCost );
						break;
					case "itemId-22":
						raschelFabricDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+raschelFabricDBCost );
						break;
					case "itemId-23":
						lenoFabricDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+lenoFabricDBCost );
						break;
					case "itemId-24":
						condcutiveFabricDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+condcutiveFabricDBCost );
						break;
					case "itemId-25":
						condcutiveFabricDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+condcutiveFabricDBCost );
						break;
					case "itemId-26":
						linerDBRWCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+linerDBRWCost );
						break;
					case "itemId-27":
						linerDBCCCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+linerDBCCCost );
						break;
					case "itemId-28":
						linerDBfobCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+linerDBfobCost );
						break;
					case "itemId-29":
						linerDBcifCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+linerDBcifCost );
						break;
					case "itemId-30":
						linerDBPalletCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+linerDBPalletCost );
						break;
					case "itemId-31":
						linerTabbingDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+linerTabbingDBCost );
						break;
					case "itemId-32":
						linerGluingDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+linerGluingDBCost );
						break;
					case "itemId-33":
						preShapeLinerDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+preShapeLinerDBCost );
						break;
					case "itemId-34":
						bottolShapeLinerDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+bottolShapeLinerDBCost );
						break;
					case "itemId-35":
						formFitLinerDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+formFitLinerDBCost );
						break;
					case "itemId-36":
						transperentFabricLinerDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+transperentFabricLinerDBCost );
						break;
					case "itemId-37":
						flangesLinerDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+flangesLinerDBCost );
						break;
					case "itemId-38":
						unDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+unDBCost );
						break;
					case "itemId-39":
						multifilamentDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+multifilamentDBCost );
						break;
					case "itemId-40":
						fabirilatedLoopDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+fabirilatedLoopDBCost );
						break;
					case "itemId-41":
						ppLoopDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+ppLoopDBCost );
						break;
					case "itemId-42":
						polysterDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+polysterDBCost );
						break;
					case "itemId-43":
						labelDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+labelDBCost );
						break;
					case "itemId-44":
						otherLabelDBCost = item_cost;
						System.out.println("Type : "+costTableIterator.getItemName()+ " ,Item Cost : "+otherLabelDBCost );
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (result) {
				double additionalValue = 0;

				totalCost = totalCost + bagDBRWCost;
				System.out.println("bagRawMaterialCost : "+bagDBRWCost);

				// checking user
				if (currentUser.getRole().getRoleAbbr().equalsIgnoreCase(ROLE.DIST.name())) {
					totalCost = totalCost + bagDBCCCost + additionalValue;
					System.out.println("bagDBCCCost end user : "+ totalCost);

				} else {
					totalCost = totalCost + bagDBCCCost;
					System.out.println("bagDBCCCost : "+ totalCost);
				}

				// checking uv status
				if (enquiryTemplateBean.isUv()) {
					totalCost = totalCost + uvDBCost;
					System.out.println("uvDBCost : "+ totalCost);
				}

				// checking packaging details
				if (enquiryTemplateBean.getPackagingType().getPackagingTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.PACKAGING_TYPE_PALLET)) {
					totalCost = totalCost + palletDBCost;
					System.out.println("palletDBCost : "+ totalCost);
				}

				// checking shipment type
				if (enquiryTemplateBean.getShipmentType().getShipmentTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.SHIPMENT_TYPE)) {
					totalCost = totalCost + fobDBCost;
					System.out.println("bagShipmentCost fob : "+totalCost);
				}

				// checking top filling status
				if (enquiryTemplateBean.isTopFilling()) {
					totalCost = totalCost + attachmentTopDBCost;
					System.out.println("attachmentTopDBCost : "+totalCost);
					if (enquiryTemplateBean.getTopType().getTopTypeAbbr()
							.equalsIgnoreCase(ENQUIRY.TOP_TYPE_CONICAL_TOP)) {
						totalCost = totalCost + conicalTopDBCost;
						System.out.println("conicalTopDBCost : "+totalCost);
					}
				}

				// checking bottom discharge
				if (enquiryTemplateBean.isBottomDischarge()) {
					totalCost = totalCost + attachmentBottomDBCost;
					System.out.println("attachmentBottomDBCost : "+totalCost);
					if (enquiryTemplateBean.getBottomType().getBottomTypeAbbr()
							.equalsIgnoreCase(ENQUIRY.BOTTOM_TYPE_CONICAL_BOTTOM)) {
						totalCost = totalCost + conicalBottomDBCost;
						System.out.println("conicalBottomDBCost : "+totalCost);
					}
				}

				// checking fabric color count
				if (fabricColorCount > 1) {
					totalCost = totalCost + colorFabricDBCost;
					System.out.println("colorFabricDBCost : "+totalCost);
				}

				// checking baffle status
				if (enquiryTemplateBean.isBaffle()) {
					totalCost = totalCost + baffelDBCost;
					System.out.println("baffelDBCost : "+totalCost);
				}

				// checking bag seam type
				if (enquiryTemplateBean.getFabricBagSeamType().getBagseamTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.BAG_SEAM_TYPE_SINGLE_FILLER_CORD)) {
					totalCost = totalCost + singleFillerDBCost;
					System.out.println("singleFillerDBCost : "+totalCost);
				} else if (enquiryTemplateBean.getFabricBagSeamType().getBagseamTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.BAG_SEAM_TYPE_DOUBLE_FILLER_CORD)) {
					totalCost = totalCost + doubleFillerDBCost;
					System.out.println("doubleFillerDBCost : "+totalCost);
				} else if (enquiryTemplateBean.getFabricBagSeamType().getBagseamTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.BAG_SEAM_TYPE_TRIPLE_FILLER_CORD)) {
					totalCost = totalCost + trippleFillerDBCost;
					System.out.println("trippleFillerDBCost : "+totalCost);
				}

				// checking printing
				if (enquiryTemplateBean.isPrinting()) {
					if (printingSide > 0) {
						bagPrintCost = printDBCost * printingSide;
						System.out.println("printingSide : "+printingSide+ " Printing Price: " +bagPrintCost);
					}
				}

				// checking extra cleaning
				if (enquiryTemplateBean.isExtraCleaning()) {
					totalCost = totalCost + extraCleaningDBCost;
					System.out.println("extraCleaningDBCost : "+totalCost);
				}

				// checking metal detector
				if (enquiryTemplateBean.isMetalDetector()) {
					totalCost = totalCost + metalDetectorDBCost;
					System.out.println("metalDetectorDBCost : "+totalCost);
				}

				// checking fabric type
				if (enquiryTemplateBean.getFabricType().getFabricTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.FABRIC_TYPE_VENTILATED_FABRIC)) {
					totalCost = totalCost + ventilatedFabricDBCost;
					System.out.println("ventilatedFabricDBCost : "+totalCost);
				} else if (enquiryTemplateBean.getFabricType().getFabricTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.FABRIC_TYPE_SULZER_FABRIC)) {
					totalCost = totalCost + sulzerFabricDBCost;
					System.out.println("sulzerFabricDBCost : "+totalCost);
				} else if (enquiryTemplateBean.getFabricType().getFabricTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.FABRIC_TYPE_RASCHEL_FABRIC)) {
					totalCost = totalCost + raschelFabricDBCost;
					System.out.println("russelFabricDBCost : "+totalCost);
				} else if (enquiryTemplateBean.getFabricType().getFabricTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.FABRIC_TYPE_LENO_FABRIC)) {
					totalCost = totalCost + lenoFabricDBCost;
					System.out.println("lenoFabricDBCost : "+totalCost);
				} else if (enquiryTemplateBean.getFabricType().getFabricTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.FABRIC_TYPE_CONDUCTIVE_FABRIC)) {
					totalCost = totalCost + condcutiveFabricDBCost;
					System.out.println("condcutiveFabricDBCost : "+totalCost);
				} else if (enquiryTemplateBean.getFabricType().getFabricTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.FABRIC_TYPE_BTYPE_FABRIC)) {
					totalCost = totalCost + bTypeFabricDBCost;
					System.out.println("bTypeFabricDBCost : "+totalCost);
				}

				// checking top block
				if (enquiryTemplateBean.isTopBlock()) {
					topBlockCost = topBlockCount * bLockDBCost;
					System.out.println("TopBlockCount: " +topBlockCount+ " topBlockCost: " +topBlockCost);
				}

				// checking bottom block
				if (enquiryTemplateBean.isBottomBlock()) {
					bottomBlockCost = bottomBlockCount * bLockDBCost;
					System.out.println("BottomBlockCount: " +bottomBlockCount+ " bottomBlockCost: " +bottomBlockCost);
				}

				// checking top velcro
				if (enquiryTemplateBean.isTopVelcroTie()) {
					topVelcroCost = topVelcroCount * velcroTieCost;
					System.out.println("TopVelcroCount: " +topVelcroCount+ " topVelcroCost: " +topVelcroCost);
				}

				// checking bottom velcro
				if (enquiryTemplateBean.isBottomVelcroTie()) {
					bottomVelcroCost = bottomVelcroCount * velcroTieCost;
					System.out.println("BottomVelcroCount: " +bottomVelcroCount+ " bottomVelcroCost: " +bottomVelcroCost);
				}

				// checking docPouchType
				if (enquiryTemplateBean.getDocPouchType().getDocPouchTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.DOC_POUCH_TYPE_ZIP_LOCK)) {
					bagDocTypeCost = docCount * zipLockDBCost;
					System.out.println("DocPouchType Cost: " +bagDocTypeCost+ "DocPouchCount: " +docCount);
				}

				// checking if liner
				if (enquiryTemplateBean.isLiner()) {
					totalCost = totalCost + linerDBRWCost;
					System.out.println("linerRawMaterialCost  : "+totalCost);
					totalCost = totalCost + linerDBCCCost;
					System.out.println("linerConvertionCost  : "+totalCost);
					
					if (enquiryTemplateBean.getShipmentType().getShipmentTypeAbbr()
							.equalsIgnoreCase(ENQUIRY.SHIPMENT_TYPE)) {
						totalCost = totalCost + linerDBfobCost;
						System.out.println("linerShipmentCost fob  : "+totalCost);
					}
					if (enquiryTemplateBean.getPackagingType().getPackagingTypeAbbr()
							.equalsIgnoreCase(ENQUIRY.PACKAGING_TYPE_PALLET)) {
						totalCost = totalCost + linerDBPalletCost;
						System.out.println("linerDBPalletCost : "+totalCost);
					}
					if (enquiryTemplateBean.isLinerTabbing()) {
						bagLinerTabbingCost = linerTabbingCount * linerTabbingDBCost;
						System.out.println("LinerTabbingCost " +bagLinerTabbingCost);
					}
					if (enquiryTemplateBean.isLinerGlued()) {
						bagLinerGluingCost = linerGluingDBCost;
						System.out.println("LinerGluingCost " +bagLinerGluingCost);
					}
					if (enquiryTemplateBean.getLinerType().getLinerTypeAbbr()
							.equalsIgnoreCase(ENQUIRY.LINER_TYPE_PRE_SHAPE)) {
						bagLinerTypeCost = preShapeLinerDBCost;
						System.out.println("LinerTypeCost " +preShapeLinerDBCost);
					} else if (enquiryTemplateBean.getLinerType().getLinerTypeAbbr()
							.equalsIgnoreCase(ENQUIRY.LINER_TYPE_BOTTLE_SHAPE)) {
						bagLinerTypeCost = bottolShapeLinerDBCost;
						System.out.println("LinerTypeCost " +bottolShapeLinerDBCost);
					} else if (enquiryTemplateBean.getLinerType().getLinerTypeAbbr()
							.equalsIgnoreCase(ENQUIRY.LINER_TYPE_FORM_FIT)) {
						bagLinerTypeCost = formFitLinerDBCost;
						System.out.println("LinerTypeCost " +formFitLinerDBCost);
					}
				}

				// loop material
				/*if (enquiryTemplateBean.getLoopMaterial().getLoopMaterialAbbr()
						.equalsIgnoreCase(ENQUIRY.LOOP_MATERIAL_MULTIFILAMENT_LOOP)) {
					totalCost = totalCost + multifilamentDBCost;
					System.out.println("multifilamentDBCost : "+totalCost);
				} else if (enquiryTemplateBean.getLoopMaterial().getLoopMaterialAbbr()
						.equalsIgnoreCase(ENQUIRY.LOOP_MATERIAL_FABRILATED)) {
					totalCost = totalCost + fabirilatedLoopDBCost;
					System.out.println("fabirilatedLoopDBCost : "+totalCost);
				} else if (enquiryTemplateBean.getLoopMaterial().getLoopMaterialAbbr()
						.equalsIgnoreCase(ENQUIRY.LOOP_MATERIAL_POLYSTER)) {
					totalCost = totalCost + polysterDBCost;
					System.out.println("polysterDBCost : "+totalCost);
				}*/

				// label cost
				if (labelCount > 0) {
					bagLabelCost = labelDBCost;
					System.out.println("labelDBCost : "+labelDBCost);
				}

				if (labelCount > 1) {
					labelCount = labelCount - 1;
					bagOtherLabelCost = otherLabelDBCost * labelCount;
					System.out.println("otherLabelDBCost : "+otherLabelDBCost);
					System.out.println("bagOtherLabelCost : "+bagOtherLabelCost);
				}

				// checking un cost
				if (enquiryTemplateBean.isUn()) {
					bagUNCost = unDBCost;
					System.out.println("UNCost " +bagUNCost);
				}

				System.out.println("bagLinerTypeCost : "+bagLinerTypeCost);
				System.out.println("bagLinerGluingCost : "+bagLinerGluingCost);
				System.out.println("bagLinerTabbingCost : "+bagLinerTabbingCost);
				System.out.println("bagDocTypeCost : "+bagDocTypeCost);
				System.out.println("bottomBlockCost : "+bottomBlockCost);
				System.out.println("bagFabricTypeCost : "+bagFabricTypeCost);
				System.out.println("bagPrintCost : "+bagPrintCost);
				System.out.println("bagLabelCost : "+bagLabelCost);
				System.out.println("bagOtherLabelCost : "+bagOtherLabelCost);
				
				System.out.println(" total cost addition : "+ totalCost);
				
				totalCost = (totalCost / 1000) * bagWeight;
				
				System.out.println(" total calculated cost : "+ totalCost);
				
				totalCost = totalCost + bagLinerTypeCost + bagLinerGluingCost + bagLinerTabbingCost + bagDocTypeCost
						+ topBlockCost + bottomBlockCost + topVelcroCost + bottomVelcroCost + bagFabricTypeCost
						+ bagPrintCost + bagLabelCost + bagOtherLabelCost;
				System.out.println("Total bag cost : " + totalCost);
				enquiryTemplateBean.setTotalBagCost(totalCost);
			}
		}
		
		return enquiryTemplateBean;
	}

	/**
	 * 
	 * Supporting methods
	 * 
	 */

	//1. bag tube
	private Double calculateBagTube(Double surfaceLength, Double surfaceWidth, Double surfaceHeight, Double fabricGSM, Double loopHeight) {
		Double bagTube = 0.0;
		Double openLoopHeight = calculateOpenLoopHeight(surfaceHeight, loopHeight);
		bagTube = (2 * ((surfaceWidth + surfaceLength) * openLoopHeight * fabricGSM)) / 10000;
		System.out.println("bag tube: "+bagTube);
		return bagTube;
	}
		//1.a open loop height
		private Double calculateOpenLoopHeight(Double surfaceHeight, Double loopHeight) {
			Double openLoopHeight = 0.0;
			openLoopHeight = (surfaceHeight + loopHeight) + 22;
			System.out.println("open loop height: "+openLoopHeight);
			return openLoopHeight;
		}
	
	//2. bag base
	private Double calculateBagBase(Double surfaceLength, Double surfaceWidth, Double fabricGSM) {
		Double bagBase = 0.0;
		bagBase = ((surfaceLength + 12) * (surfaceWidth + 12) * fabricGSM) / 10000;
		System.out.println("bag base: "+bagBase);
		return bagBase;
	}
	
	// 3. Calculate Attachment Inlet
	// 3.a. Top Skirt
	private Double calculateAttachmentTopSkirt(Double surfaceLength, Double surfaceWidth, Double topSkirtLength, Double topSkirtGSM) {
		Double topSkirtCutLength = calculateTopSkirtCutLength(surfaceLength, surfaceWidth);
		Double topSkirt = ((topSkirtLength + 5) * topSkirtCutLength * topSkirtGSM) / 10000;
		System.out.println("top skirt: "+topSkirt);
		return topSkirt;
	}
		// 3.a.1 Calculate Top Skirt Cut Length
		private Double calculateTopSkirtCutLength(Double surfaceLength, Double surfaceWidth) {
			Double topSkirtCutLength = ((surfaceLength + surfaceWidth) * 2) + 12;
			System.out.println("top skirt cut length: "+topSkirtCutLength);
			return topSkirtCutLength;
		}
	
	// 3.b Top Spout
	private Double calculateAttachmentTopSpout(Double surfaceLength, Double surfaceWidth, Double topSpoutLength,Double topSpoutGSM, Double topSpoutDiameter) {
		Double topSpoutCutLength = calculateTopSpoutCutLength(topSpoutDiameter);
		Double topSpout = ((topSpoutLength + 5) * topSpoutCutLength * topSpoutGSM) / 10000;
		System.out.println("top spout: "+topSpout);
		return topSpout;
	}
		// 3.b.1 Calculate Top Spout Cut Length
		private Double calculateTopSpoutCutLength(Double topSpoutDiameter) {
			Double TopSpoutCutLength = (topSpoutDiameter * 3.14) + 12;
			System.out.println("top spout cut length: "+TopSpoutCutLength);
			return TopSpoutCutLength;
		}
		// 3.b.2
		private Double calculateAttachmentTopFabric(Double surfaceLength, Double surfaceWidth, Double topSpoutGSM) {
			Double topFabric = ((surfaceLength + 12) * (surfaceWidth + 12) * topSpoutGSM) / 10000;
			System.out.println("top fabric: "+topFabric);
			return topFabric;
		}
		
	// 3.c Top Flap
	private Double calculateAttachmentTopFlap(Double surfaceLength, Double surfaceWidth, Double topFlapGSM) {
		Double topFlap = ((surfaceLength + 12) * (surfaceWidth + 12) * topFlapGSM) / 10000;
		System.out.println("top flap: "+topFlap);
		return topFlap;
	}
	
	// 3.d. Conical Top
	private Double calculateAttachmentConicalTop(Double surfaceLength, Double surfaceWidth) {
		// ((BAG WIDTH +12)+10 + (Bag length +12)+10 )/10000
		Double conicalTop = (((surfaceLength + 12) + 10) + ((surfaceWidth + 12) + 10)) / 10000;
		System.out.println("conical top: "+conicalTop);
		return conicalTop;
	}
	
	// 3.e. Platted Top
	private Double calculateAttachmentPlattedTop(Double surfaceLength, Double surfaceWidth) {
		Double plattedTop = (((surfaceWidth + 12) + 18) + (surfaceLength + 12) + 18) / 10000;
		System.out.println("platted top: "+plattedTop);
		return plattedTop;
	}
	
	// 4. Calculate Attachment Outlet
	// 4.a calculate Attachment Outlet Bottom Spout
	private Double calculateAttachmentOutletBottomSpout(Double bottomSpoutDiameter, Double bottomSpoutGSM,Double bottomSpoutLength, Double surfaceLength, Double surfaceWidth, Double surfaceHeight,String bottomTypeAbbr) {
		Double bottomSpoutCutLength = calculateBottomSpoutCutLength(bottomSpoutDiameter);
		Double bottomAttach = 0.0;
	
		// a. bottom spout standard
		if (bottomTypeAbbr.equalsIgnoreCase(ENQUIRY.BOTTOM_TYPE_BOTTOM_SPOUT_STANDARD)) {
			bottomAttach = ((bottomSpoutLength + 5) * bottomSpoutCutLength * bottomSpoutGSM) / 10000;
			
			// b. bottom spout petal
		} else if (bottomTypeAbbr.equalsIgnoreCase(ENQUIRY.BOTTOM_TYPE_BOTTOM_SPOUT_PETAL)) {
			Double bottomSpoutStandard = ((bottomSpoutLength + 5) * bottomSpoutCutLength * bottomSpoutGSM) / 10000;
			bottomAttach = bottomSpoutStandard + 25;  
			
			// c. discharge spout, empty spout with iris, pyjama, bonnet closure		
		} else if (bottomTypeAbbr.equalsIgnoreCase(ENQUIRY.BOTTOM_TYPE_DISCHARGE_SPOUT)
				|| bottomTypeAbbr.equalsIgnoreCase(ENQUIRY.BOTTOM_TYPE_EMPTY_SPOUT_WITH_IRIS)
				|| bottomTypeAbbr.equalsIgnoreCase(ENQUIRY.BOTTOM_TYPE_PYJAMA)
				|| bottomTypeAbbr.equalsIgnoreCase(ENQUIRY.BOTTOM_TYPE_BONNET_CLOSURE)) {
			Double bottomSpoutCal = (bottomSpoutDiameter / 2) + (bottomSpoutLength + 17);
			bottomAttach = (bottomSpoutCal * bottomSpoutCutLength * bottomSpoutGSM)/10000;
			
			// d. conical bottom
		} else if (bottomTypeAbbr.equalsIgnoreCase(ENQUIRY.BOTTOM_TYPE_CONICAL_BOTTOM)) {
			bottomAttach= (surfaceHeight + 50 + 12) / 10000;
		
		// e. full discharge
		}else if (bottomTypeAbbr.equalsIgnoreCase(ENQUIRY.BOTTOM_TYPE_FULL_DISCHARGE_BOTTOM)) {
			Double bottomSkirtLength=0.0, bottomSkirtCutLength=0.0, bottomFabric=0.0;
			bottomSkirtCutLength= ((surfaceLength + surfaceWidth) * 2) + 12;
			bottomSkirtLength= (bottomSpoutLength + 5) * bottomSkirtCutLength * bottomSpoutGSM ;
			bottomFabric = 2*(surfaceLength + 12) * (surfaceWidth + 12) * bottomSpoutGSM;
			bottomAttach = (bottomSkirtLength + bottomFabric)/10000;
		}
		System.out.println("bottom attach: "+bottomAttach);
		return bottomAttach;
	}
		// 4.a.1 Calculate Bottom Spout Cut Length
		private Double calculateBottomSpoutCutLength(Double bottomSpoutDiameter) {
			Double bottomSpoutCutLength = ((bottomSpoutDiameter * 3.14) + 12);
			System.out.println("bottom spout cut length: "+bottomSpoutCutLength);
			return bottomSpoutCutLength;
		}
		
	//5.a bag thread
	private Double calculateThreadWithoutTopBottom(Double surfaceLength, Double surfaceWidth) {
		Double thread = (((surfaceLength * 2) + (surfaceWidth * 2)) / 100) * 5;
		return thread;
	}
	
	//5.b bag thread
	private Double calculateThreadForBag(Double surfaceLength, Double surfaceWidth, Double surfaceHeight,
			boolean topFilling, String topFillingTypeAbbr, Double inletLength, Double inletDiameter,
			boolean bottomDischarge, Double outletLength, Double outletDiameter) {
		Double thread = 0.0, topThread = 0.0, bottomThread = 0.0, topCutLength = 0.0;
		if (topFilling) {
			if (topFillingTypeAbbr.equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_SKIRT)
					|| topFillingTypeAbbr.equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_SKIRT_WITH_FLAP)) {
				topCutLength = 0.0;
			} else if (topFillingTypeAbbr.equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_SPOUT)
					|| topFillingTypeAbbr.equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_SPOUT_WITH_FLAP)) {
				topCutLength = calculateTopSpoutCutLength(inletDiameter);
			}
			topThread = (inletLength + 5) + topCutLength;
		}
		if (bottomDischarge) {
			bottomThread = (outletLength + 5) + calculateBottomSpoutCutLength(outletDiameter);
		}
		thread = (((surfaceLength * 2) + (surfaceWidth * 2) + (surfaceHeight * 2) + topThread + bottomThread) / 100) * 5;
		return thread;
	}
	
	//6. liner
	//6.a liner tube
	private Double calculateLinerTube(Double surfaceLength, Double surfaceWidth) {
		Double linerTube = 0.0;
		linerTube= surfaceLength + surfaceWidth  + 5;
		System.out.println("LinerTube= "+linerTube);
		return linerTube;
	}
	//6.b Liner Height
	private Double calculateLinerHeight(Double surfaceWidth, Double surfaceHeight, Double topLen, Double bottomLen, String topFillingTypeAbbr, String linerTypeAbbr) {
		Double linerHeight = 0.0;
		if (topFillingTypeAbbr.equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_SKIRT)) {
			surfaceWidth = surfaceWidth / 2;
		}
		linerHeight = surfaceWidth + surfaceHeight +  (topLen + 5 ) + (bottomLen + 5 ); 
		
		if (linerTypeAbbr.equalsIgnoreCase(ENQUIRY.LINER_TYPE_LOOSELY_FIT)) {
			linerHeight = linerHeight + 20;
		}
		System.out.println("liner height: "+linerHeight);
		return linerHeight;
	}
	//6.c liner
	private Double calculateLiner(Double linerMicronValue, Double linerTubeValue, Double linerHeightValue) {
		Double liner = linerTubeValue * linerHeightValue * 2 * (linerMicronValue *  0.923) / 10000;
		System.out.println("liner= "+liner);
		return liner;
	}
	
	//7
	//7.a filler cord
	private Double calculateFillerCord(Double bagThread, String bagseamTypeAbbr) {
		double fillerCord = 0;
		if (bagseamTypeAbbr.equalsIgnoreCase(ENQUIRY.BAG_SEAM_TYPE_SINGLE_FILLER_CORD)) {
			fillerCord = bagThread * 1;
		} else if (bagseamTypeAbbr.equalsIgnoreCase(ENQUIRY.BAG_SEAM_TYPE_DOUBLE_FILLER_CORD)) {
			fillerCord = bagThread * 2;
		} else if (bagseamTypeAbbr.equalsIgnoreCase(ENQUIRY.BAG_SEAM_TYPE_TRIPLE_FILLER_CORD)) {
			fillerCord = bagThread * 3;
		}
		System.out.println("filler cord: "+fillerCord);
		return fillerCord;
	}
	
	//7.b  tie for top
	private int tieForTop(EnquiryTemplateBean enquiryTemplateBean) {
		if (!enquiryTemplateBean.isTopStandardTie()) {
			enquiryTemplateBean.setTopStandardTieNumber(0.0);
		}
		if (!enquiryTemplateBean.isTopRopeTie()) {
			enquiryTemplateBean.setTopRopeTieNumber(0.0);
		}
		if (!enquiryTemplateBean.isTopVelcroTie()) {
			enquiryTemplateBean.setTopVelcroTieNumber(0.0);
		}
		if (!enquiryTemplateBean.isTopBlock()) {
			enquiryTemplateBean.setTopBlockNumber(0.0);
		}
		int totalTopTie = (int) (enquiryTemplateBean.getTopStandardTieNumber()
				+ enquiryTemplateBean.getTopRopeTieNumber() + enquiryTemplateBean.getTopVelcroTieNumber()
				+ enquiryTemplateBean.getTopBlockNumber());

		return 10 * totalTopTie;
	}
	
	//7.c tie for bottom
	private int tieForBottom(EnquiryTemplateBean enquiryTemplateBean) {
		if (!enquiryTemplateBean.isBottomStandardTie()) {
			enquiryTemplateBean.setBottomStandardTieNumber(0.0);
		}
		if (!enquiryTemplateBean.isBottomRopeTie()) {
			enquiryTemplateBean.setBottomRopeTieNumber(0.0);
		}
		if (!enquiryTemplateBean.isBottomVelcroTie()) {
			enquiryTemplateBean.setBottomVelcroTieNumber(0.0);
		}
		if (!enquiryTemplateBean.isBottomBlock()) {
			enquiryTemplateBean.setBottomBlockNumber(0.0);
		}
		int totalBottomTie = (int) (enquiryTemplateBean.getBottomStandardTieNumber()
				+ enquiryTemplateBean.getBottomRopeTieNumber() + enquiryTemplateBean.getBottomVelcroTieNumber()
				+ enquiryTemplateBean.getBottomBlockNumber());

		if (enquiryTemplateBean.getBottomDischargeType().getBottomDischargeTypeAbbr()
				.equalsIgnoreCase(ENQUIRY.BOTTOM_TYPE_BOTTOM_SPOUT_PETAL)
				|| enquiryTemplateBean.getBottomDischargeType().getBottomDischargeTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.BOTTOM_TYPE_EMPTY_SPOUT_WITH_IRIS)) {
			int tieForBottom = totalBottomTie * 20;
			return tieForBottom;
		} else {
			int tieForBottom = totalBottomTie * 10;
			return tieForBottom;
		}
	}
	
	//7.d document
	private int totalDocument(Double docPouchValue) {
		int document = (int) (docPouchValue * 10);
		System.out.println("document: "+document);
		return document;
	}
	
	//7.e label 
	private int totalLabel(int numberofLabels) {
		int label = numberofLabels * 5;
		System.out.println("label: "+label);
		return label;
	}
	
	//8 perimeter band
	private double calculatePerimeterBand(Double surfaceLength, Double surfaceWidth, Double swl) {
		double perimeterBand = 0, gramage = 23;
		if(swl< 1250)
		 {
			 gramage=15;
		 }
		 perimeterBand = (((surfaceWidth * 2) + (surfaceLength * 2) + 25) / 100) * gramage;
		 System.out.println("perimeter band: "+perimeterBand);
		 return perimeterBand;
	}
	
	//9 Calculate Belly Band
	private double calculateBellyBand(Double surfaceLength, Double surfaceWidth) {
		double bellyBand=0;
		bellyBand= (((surfaceWidth * 2)+ (surfaceLength * 2) + 25) / 100) * 25;
		System.out.println("belly band: "+bellyBand);
		return bellyBand;
	}
	
	//10  calculate Steevdoor Strap
	private double calculateSteevdoorStrap(Double surfaceLength, Double surfaceWidth, Double swl, int sfTypeValue) {
		double steevdoorStrap = 0, gramage = 0;
		if (sfTypeValue > 5) {
			gramage = setCrossLoopGramFromRange(swl);
		} else {
			gramage = setCrossLoopGramForSFFiveFromRange(swl);
		}
		steevdoorStrap = (((surfaceLength + surfaceWidth + 25) * 2) / 100) * gramage;
		System.out.println("steev door strap :"+steevdoorStrap);
		return steevdoorStrap;
	}
		
	//10.a
		private double setCrossLoopGramFromRange(double bagSWL) {
			// TODO Auto-generated method stub
			if (bagSWL <= 499) {
				return 25 ;
			} else if (bagSWL >= 500 && bagSWL <= 600) {
				return 28;
			} else if (bagSWL >= 601 && bagSWL <= 850) {
				return 30;
			} else if (bagSWL >= 851 && bagSWL <= 1150) {
				return 32;
			} else if (bagSWL >= 1151 && bagSWL <= 1350) {
				return 42;
			} else if (bagSWL >= 1351 && bagSWL <= 1650) {
				return 45;
        	}else if (bagSWL >= 1651 && bagSWL <= 1850) {
        		return  55;
        	}else if (bagSWL > 1850) {
        		return 60;
        	}else{
        		return 0;
        	}
		}
		//10.b
		private double setCrossLoopGramForSFFiveFromRange(double bagSWL) {
			// TODO Auto-generated method stub
			if (bagSWL <= 499) {
	         	return 25 ;
	         } else if (bagSWL >= 500 && bagSWL <= 600) {
	        	 return 30;
	         } else if (bagSWL >= 601 && bagSWL <= 850) {
	        	 return 32;
	         } else if (bagSWL >= 851 && bagSWL <= 1150) {
	        	 return 42;
	         } else if (bagSWL >= 1151 && bagSWL <= 1350) {
	        	 return 45;
	         } else if (bagSWL >= 1351 && bagSWL <= 1650) {
	        	 return 50;
	         }else if (bagSWL >= 1651 && bagSWL <= 1850) {
	        	 return  60;
	         }else if (bagSWL > 1850) {
	        	 return 65;
	         }else{
	        	 return 0;
	         }
		}
		
	//11. calculate Baffle Weight
	private double calculateBaffleWeight(Double surfaceLength, Double surfaceWidth, Double surfaceHeight, Double baffleValue) {
		System.out.println("baffle value: "+baffleValue);
		double baffleWidth = ((((surfaceLength + surfaceWidth) / 2) / 3) * 1.41 ) + 7;
		double baffleWeight = (4 * (baffleWidth * (surfaceHeight - 16) * baffleValue)) / 10000;
		System.out.println("baffle width: "+baffleWidth);
		System.out.println("Baffle Weight : " + baffleWeight);
		return baffleWeight;
	}
	
	//12. calculate Loop Protector
	private double calculateLoopProtector(Double loopHeight, Double loopProtectorValue, Double loopNumber) {
		double loopProtector=0, protectorCutLength=0;
		protectorCutLength= loopHeight * 2 * loopNumber;
		loopProtector= (17 * loopNumber) * protectorCutLength * loopProtectorValue / 10000;
		System.out.println("loop protector: "+loopProtector);
		return loopProtector;
	}
}
