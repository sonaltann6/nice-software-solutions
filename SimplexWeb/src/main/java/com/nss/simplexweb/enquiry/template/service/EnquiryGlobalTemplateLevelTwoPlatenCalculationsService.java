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

@Service("enquiryGlobalTemplateLevelTwoPlatenCalculationsService")
public class EnquiryGlobalTemplateLevelTwoPlatenCalculationsService {

	@Autowired
	private FibcCostTableService fibcCostTableService;

	// Calculate Bag Weight
	public EnquiryTemplateBean calculateBagWeight(EnquiryTemplateBean enquiryTemplateBean) {
		
		Double bagWeight = 0.0;
		Double bagBody=0.0,bagSide=0.0,bagTop=0.0,bagLoop=0.0,bagThread=0.0,fillerCord=0.0,bagLiner=0.0;
		int tieForTop=0,document=0,label=0;
		
		//platen body
		bagBody = calculatePlatenBody(enquiryTemplateBean.getSurfaceLength(), enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getSurfaceHeight(), enquiryTemplateBean.getFabricGSMValue(),enquiryTemplateBean.getProductSurfaceType().getSurfaceTypeAbbr());
		System.out.println("bagBody : "+bagBody);
		//bag side
		bagSide = calculateBagSide(enquiryTemplateBean.getSurfaceLength(), enquiryTemplateBean.getSurfaceHeight(), enquiryTemplateBean.getFabricGSMValue(),enquiryTemplateBean.getProductSurfaceType().getSurfaceTypeAbbr());
		System.out.println("bagSide : "+bagSide);
		//bag loop
		bagLoop = calculateBagLoop(enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getSurfaceHeight(), enquiryTemplateBean.getSwl(),enquiryTemplateBean.getProductSFtype().getSfTypeValue(), enquiryTemplateBean.getLoopHeight(),enquiryTemplateBean.getLoopNumber());
		System.out.println("bagLoop : "+bagLoop);
		//bag top filling
		if(enquiryTemplateBean.isTopFilling()) {
			if(enquiryTemplateBean.getTopType().getTopTypeAbbr().equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_FLAP)) {
				bagTop = calculatePlatenBagTop(enquiryTemplateBean.getSurfaceLength(), enquiryTemplateBean.getSurfaceWidth(),enquiryTemplateBean.getTopFlapGSM(), enquiryTemplateBean.getProductSurfaceType().getSurfaceTypeAbbr());
			}else if(enquiryTemplateBean.getTopType().getTopTypeAbbr().equalsIgnoreCase(ENQUIRY.TOP_TYPE_TOP_SKIRT_WITH_FLAP)) {
				bagTop = calculatePlatenBagTop(enquiryTemplateBean.getSurfaceLength(), enquiryTemplateBean.getSurfaceWidth(),enquiryTemplateBean.getTopFlapGSM(), enquiryTemplateBean.getProductSurfaceType().getSurfaceTypeAbbr());
				bagTop = bagTop + calculateBagTopSkirt(enquiryTemplateBean.getSurfaceLength(), enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getTopSkirtLength(), enquiryTemplateBean.getTopSkirtGSM(), enquiryTemplateBean.getProductSurfaceType().getSurfaceTypeAbbr()); 
			}
		}
		
		//liner
		Double linerWidth = 0.0, linerLength = 0.0;
		if(enquiryTemplateBean.isLiner()) {
			linerWidth = calculateBagLinerWidth(enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getSurfaceHeight(), enquiryTemplateBean.getTopSkirtLength());
			linerLength = calculateBagLinerLength(enquiryTemplateBean.getSurfaceLength(), enquiryTemplateBean.getSurfaceHeight(), enquiryTemplateBean.getTopSkirtLength());
			bagLiner = calculateBagLiner(linerLength, linerWidth, enquiryTemplateBean.getLinerMicronValue());
		}
		
		//bag thread
		bagThread = calculateBagThread(enquiryTemplateBean.getSurfaceLength(), enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getSurfaceHeight(),enquiryTemplateBean.getProductSurfaceType().getSurfaceTypeAbbr());
		System.out.println("bag thread : "+bagThread);
		
		//filler cord
		fillerCord = calculateFillerCord(bagThread, enquiryTemplateBean.getFabricBagSeamType().getBagseamTypeAbbr());
		System.out.println("bag filler cord: "+fillerCord);
		
		//tie for top
		tieForTop = calculateTieForTop(enquiryTemplateBean);
		System.out.println("tie for top: "+tieForTop);
		
		//document
		document = calculateDocument((enquiryTemplateBean.isDocPouch()) ? enquiryTemplateBean.getDocPouchValue() : 0);
		System.out.println("document: "+document);
		
		//label
		label = calculateLabel((enquiryTemplateBean.isOtherLabels()) ? 1 : 0);
		System.out.println("label: "+label);
		
		Double bagPerimeterBand = 0.0;
		Double bagBellyBand = 0.0;
		Double bagSteevdoorStrapBand = 0.0;
		Double bagBaffelGSM = 0.0;
		Double bagLinerGlued = 0.0;
		Double bagLoopProtector = 0.0;
		
		//perimeter band
		if(enquiryTemplateBean.isPerimeterBand()) {
			bagPerimeterBand = calculatePerimeterBand(enquiryTemplateBean.getSurfaceLength(), enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getSwl());
		}
		
		//belly band
		if(enquiryTemplateBean.isBellyBand()) {
			bagBellyBand = calculateBellyBand(enquiryTemplateBean.getSurfaceLength(), enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getSwl());
		}
		
		//steev door strap
		if(enquiryTemplateBean.isSteevdoorStrap()) {
			bagSteevdoorStrapBand = calculateSteevDoorStrap(enquiryTemplateBean.getSurfaceLength(), enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getSwl(), enquiryTemplateBean.getProductSFtype().getSfTypeValue());
		}
		
		//loop protector
		if(enquiryTemplateBean.isLoopProtector()) {
			bagLoopProtector = calculateLoopProtector(enquiryTemplateBean.getLoopHeight(), enquiryTemplateBean.getLoopProtectorValue(), enquiryTemplateBean.getLoopNumber());
		}
		
		//baffle status
		if(enquiryTemplateBean.isBaffle()) {
			bagBaffelGSM = calculateBaffleWeight(enquiryTemplateBean.getSurfaceLength(), enquiryTemplateBean.getSurfaceWidth(), enquiryTemplateBean.getSurfaceHeight(), enquiryTemplateBean.getBaffleValue());
		}
		
		//liner glued
		if(enquiryTemplateBean.isLiner()) {
			if(enquiryTemplateBean.isLinerGlued()) {
				bagLinerGlued = 100.0;
			}
		}
		
		//bag weight
		bagWeight = (bagBody + bagSide + bagTop + bagLoop + bagLiner + bagThread + fillerCord + tieForTop + document + label + bagPerimeterBand + bagBellyBand + bagSteevdoorStrapBand + bagLoopProtector + bagBaffelGSM + bagLinerGlued) / 1000;
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
				double additionalValue = 150;

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
				if (fabricColorCount > 0) {
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
				if (enquiryTemplateBean.getLoopMaterial().getLoopMaterialAbbr()
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
				}

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
	
	//1. calculate platen body
	private Double calculatePlatenBody(Double surfaceLength, Double surfaceWidth, Double surfaceHeight, Double bagGSM, String productSurfaceType) {
		Double platenBody = 0.0;
		Double bagCutLength = calculateBagCutLength(surfaceLength, surfaceHeight, productSurfaceType);
		if(productSurfaceType.equalsIgnoreCase(ENQUIRY.SURFACE_TYPE_INTERNAL)) {
			platenBody = ((surfaceWidth + 12) * bagCutLength * bagGSM) / 10000;
		}else if(productSurfaceType.equalsIgnoreCase(ENQUIRY.SURFACE_TYPE_EXTERNAL)) {
			platenBody = ((surfaceWidth + 8) * bagCutLength * bagGSM) / 10000;
		}
		System.out.println("calculatePlatenBody  = "+platenBody);
		return platenBody;
	}
	
		//1.a calculate calculateBagCutLength
		private Double calculateBagCutLength(Double surfaceLength, Double surfaceHeight, String productSurfaceType) {
			Double bagCutLength = 0.0;
			if(productSurfaceType.equalsIgnoreCase(ENQUIRY.SURFACE_TYPE_INTERNAL)) {
				bagCutLength = (surfaceHeight * 2) + (surfaceLength + 12);
			}else if(productSurfaceType.equalsIgnoreCase(ENQUIRY.SURFACE_TYPE_EXTERNAL)) {
				bagCutLength = (surfaceHeight * 2) + ( surfaceLength + 8);
			}
			return bagCutLength;
		}
	
	//2. calculate bag side
	private Double calculateBagSide(Double surfaceLength, Double surfaceHeight, Double bagGSM, String productSurfaceType) {
		Double bagSide = 0.0;
		if(productSurfaceType.equalsIgnoreCase(ENQUIRY.SURFACE_TYPE_INTERNAL)) {
			bagSide = (((surfaceLength + 12) * (surfaceHeight + 12) * bagGSM ) *2) / 10000;
		}else if(productSurfaceType.equalsIgnoreCase(ENQUIRY.SURFACE_TYPE_EXTERNAL)) {
			bagSide = (((surfaceLength + 8) * (surfaceHeight + 8) * bagGSM ) * 2) / 10000;
		}
		return bagSide;
	}
	
	//3. calculate bag loop
	private Double calculateBagLoop(Double surfaceWidth, Double surfaceHeight, Double SWL, int sfType, Double loopHeight, Double loopNumber) {
		Double platenLoop = 0.0, loopGram = 0.0, loopCutLength = 0.0;
		loopGram = calculateLoopGram(SWL, sfType, loopNumber);
		loopCutLength = calculateLoopCutLength(surfaceWidth, surfaceHeight, loopHeight);
		platenLoop = (loopGram * loopCutLength) / 100;
		return platenLoop;
	}
		//3.a calculate loop gram
		private Double calculateLoopGram(Double SWL, int sfType, Double loopNumber) {
			Double loopGram = 0.0;
			loopGram = ((SWL * sfType)/ loopNumber) / 40;
			return loopGram;
		}
		//3.b calculate loop cut length
		private Double calculateLoopCutLength(Double surfaceWidth, Double surfaceHeight, Double loopHeight) {
			Double loopCutLength = 0.0;
			loopCutLength = (loopHeight * 8) + (surfaceHeight * 8) + (surfaceWidth * 2) + 10;
			return loopCutLength;
		}
		
	//4. calculate top filling
	//4.a top flap
	private Double calculatePlatenBagTop(Double surfaceLength, Double surfaceWidth, Double topFlapGSM, String productSurfaceType) {
		Double topValue = 0.0;
		if(topFlapGSM == 0) {
			topFlapGSM = 80.0;
		}
		if(productSurfaceType.equalsIgnoreCase(ENQUIRY.SURFACE_TYPE_INTERNAL)) {
			topValue = ((surfaceWidth + 12) * (surfaceLength + 12) * topFlapGSM) / 10000;
		}else if(productSurfaceType.equalsIgnoreCase(ENQUIRY.SURFACE_TYPE_EXTERNAL)) {
			topValue = ((surfaceWidth + 8 )* (surfaceLength + 8) * topFlapGSM) / 10000;
		}
		System.out.println("top value : "+topValue);
		return topValue;
	}
	//4.b top skirt with flap
	private Double calculateBagTopSkirt(Double surfaceLength, Double surfaceWidth, Double topSkirtLength,  Double topSkirtGSM, String productSurfaceType) {
		Double topSkirt = 0.0;
		if(productSurfaceType.equalsIgnoreCase(ENQUIRY.SURFACE_TYPE_INTERNAL)) {
			topSkirt = ((topSkirtLength + 5) * ((surfaceWidth * 2) + surfaceLength + 12) * topSkirtGSM) / 10000;
		}else if(productSurfaceType.equalsIgnoreCase(ENQUIRY.SURFACE_TYPE_EXTERNAL)) {
			topSkirt = ((topSkirtLength + 5) * ((surfaceWidth * 2) + surfaceLength + 8) * topSkirtGSM) / 10000;
		}
		System.out.println("top skirt value : "+topSkirt);
		return topSkirt;
	}
	
	//5. liner
	//5.a liner width
	private Double calculateBagLinerWidth(Double surfaceWidth, Double surfaceHeight, Double topSkirtLength) {
		Double linerWidth = 0.0;
		linerWidth = surfaceWidth + (surfaceHeight * 2) + (topSkirtLength * 2) + 20;
		System.out.println("liner width: "+linerWidth);
		return linerWidth;
	}
	
	//5.b liner length
	private Double calculateBagLinerLength(Double surfaceLength, Double surfaceHeight, Double topSkirtLength) {
		Double linerLength = 0.0;
		linerLength = (surfaceLength * 2) + (surfaceHeight * 2) + topSkirtLength + 20;
		System.out.println("liner length : " +linerLength);
		return linerLength;
	}
	
	//5.c liner
	private Double calculateBagLiner(Double linerLength, Double linerWidth, Double linerMicron) {
		Double linerWeight = 0.0;
		linerWeight = linerLength * linerWidth * (linerMicron * 0.923) / 10000;
		System.out.println("liner weight: " +linerWeight);
		return linerWeight;
	}
	
	//6. bag thread
	private Double calculateBagThread(Double surfaceLength, Double surfaceWidth, Double surfaceHeight, String productSurfaceType) {
		Double thread = 0.0;
		thread= (((surfaceLength * 5) + (surfaceWidth * 4) + (surfaceHeight * 4)) / 100) * 5;
		System.out.println("thread: "+thread);
		return thread;
	}
	
	//7. filler cord
	private Double calculateFillerCord(Double bagThread, String bagSeamType) {
		Double fillerCord = 0.0;
		if (bagSeamType.equalsIgnoreCase(ENQUIRY.BAG_SEAM_TYPE_SINGLE_FILLER_CORD)) {
			fillerCord = bagThread * 1;
		} else if (bagSeamType.equalsIgnoreCase(ENQUIRY.BAG_SEAM_TYPE_DOUBLE_FILLER_CORD)) {
			fillerCord = bagThread * 2;
		} else if (bagSeamType.equalsIgnoreCase(ENQUIRY.BAG_SEAM_TYPE_TRIPLE_FILLER_CORD)) {
			fillerCord = bagThread * 3;
		}
		System.out.println("filler cord:" +fillerCord);
		return fillerCord;
	}
	
	//8. tie for top
	private int calculateTieForTop(EnquiryTemplateBean enquiryTemplateBean) {
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

		return totalTopTie * 6;
	}
	
	//9. document
	private int calculateDocument(Double docPouchValue) {
		int document = (int) (docPouchValue * 10);
		return document;
	}
	
	//10. label
	private int calculateLabel(int numberOfLabels) {
		int label = numberOfLabels * 5;
		return label;
	}
	
	//11. perimeter band
	private Double calculatePerimeterBand(Double surfaceLength, Double surfaceWidth, Double SWL) {
		Double perimeterBand = 0.0, gramage = 0.0;
		if(SWL < 1250){
			 gramage=15.0;
		 }
		 perimeterBand = (((surfaceWidth * 2) + (surfaceLength * 2) + 25) / 100) * gramage;
		System.out.println("perimeter band: "+perimeterBand);
		return perimeterBand;
	}
	
	//12.belly band
	private Double calculateBellyBand(Double surfaceLength, Double surfaceWidth, Double SWL) {
		Double bellyBand = 0.0;
		bellyBand= (((surfaceWidth * 2)+ (surfaceLength * 2) + 25) / 100) * 25;
		System.out.println("belly band: "+bellyBand);
		return bellyBand;
	}
	
	//13. steev door strap
	private Double calculateSteevDoorStrap(Double surfaceLength, Double surfaceWidth, Double SWL, int sfType) {
		Double steevDoorStrap = 0.0, gramage = 0.0;
		if(sfType>5){
			gramage=setCrossLoopGramFromRange(SWL);
		}
		else{
			gramage=setCrossLoopGramForSFFiveFromRange(SWL);
		}
		steevDoorStrap= (((surfaceLength + surfaceWidth + 25) * 2) / 100) * gramage;
		System.out.println("steev door strap: "+steevDoorStrap);
		return steevDoorStrap;
	}
		//13.a cross loop gram
		private Double setCrossLoopGramFromRange(Double bagSWL) {
			if (bagSWL <= 499) {
	         	return 42.0;
	         } else if (bagSWL >= 500 && bagSWL <= 600) {
	        	 return 42.0;
	         } else if (bagSWL >= 601 && bagSWL <= 850) {
	        	 return 45.0;
	         } else if (bagSWL >= 851 && bagSWL <= 1150) {
	        	 return 50.0;
	         } else if (bagSWL >= 1151 && bagSWL <= 1350) {
	        	 return 55.0;
	         } else if (bagSWL >= 1351 && bagSWL <= 1650) {
	        	 return 60.0;
	         }else if (bagSWL >= 1651 && bagSWL <= 1850) {
	        	 return 65.0;
	         }else if (bagSWL > 1850) {
	        	 return 65.0;
	         }else{
	        	 return 0.0;
	         }
		}
		//13.b cross loop gram for sf five
		private Double setCrossLoopGramForSFFiveFromRange(Double bagSWL) {
			if (bagSWL <= 499) {
	         	return 25.0;
	         } else if (bagSWL >= 500 && bagSWL <= 600) {
	        	 return 30.0;
	         } else if (bagSWL >= 601 && bagSWL <= 850) {
	        	 return 32.0;
	         } else if (bagSWL >= 851 && bagSWL <= 1150) {
	        	 return 42.0;
	         } else if (bagSWL >= 1151 && bagSWL <= 1350) {
	        	 return 45.0;
	         } else if (bagSWL >= 1351 && bagSWL <= 1650) {
	        	 return 50.0;
	         }else if (bagSWL >= 1651 && bagSWL <= 1850) {
	        	 return 60.0;
	         }else if (bagSWL > 1850) {
	        	 return 65.0;
	         }else{
	        	 return 0.0;
	         }
		}
	
	//14. loop protector
	private Double calculateLoopProtector(Double loopHeight, Double loopProtectorValue, Double loopNumber) {
		Double loopProtector = 0.0, protectorCutLength = 0.0;
		protectorCutLength= loopHeight * 2 * loopNumber;
		loopProtector= (17 * loopNumber) * protectorCutLength * loopProtectorValue / 10000;
		System.out.println("loop protector: "+loopProtector);
		return loopProtector;
	}
	
	//15.baffle weight
	private Double calculateBaffleWeight(Double surfaceLength, Double surfaceWidth, Double surfaceHeight, Double baffleValue) {
		Double baffleWeight = 0.0, baffleWidth = 0.0;
		baffleWidth = ((((surfaceLength + surfaceWidth) / 2) / 3) * 1.41 ) + 7;
		baffleWeight = (4 * (baffleWidth * (surfaceHeight - 16) * baffleValue)) / 10000;
		System.out.println("baffle weight: "+baffleWeight);
		return baffleWeight;
	}
}
