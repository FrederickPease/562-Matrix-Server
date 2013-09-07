package com.rs.utils;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.content.ItemConstants;

public final class EconomyPrices {

	public static int getPrice(int itemId) {
		Item item = new Item(itemId);
		ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
		
		if (defs.isNoted())
			itemId = defs.getCertId();
		if (!ItemConstants.isTradeable(new Item(itemId, 1)))
			return 0;
		if (itemId == 995) // TODO after here
			return 1;
			switch(itemId){
			case 4151:
				return 10000000;
			case 12155:
				return 3553;
			case 12183:
				return 100;
			case 2859:
				return 57;
			case 2138:
				return 78;
			case 5318:
				return 500;
			case 5291:
				return 5000;
			case 5295:
				return 7500;
			case 5299:
				return 10000;
			case 5302:
				return 15000;
			case 5304:
				return 25000;
			case 590:
				return 593;
			case 1755:
				return 132;
			case 1436:
				return 79;
			case 7936:
				return 324;
			case 952:
				return 5324;
			case 11283:
			case 11284:
				return 57000000;
			case 10330:
			case 10332:
			case 10334:
			case 10336:
				return 97000000;
			case 10338:
			case 10340:
			case 10342:
			case 10344:
				return 75654000;
			case 10346:
			case 10348:
			case 10350:
			case 10352:
				return 124400043;
			case 22458:
			case 22462:
			case 22466:
				return 23000000;
			case 22452:
			case 22454:
			case 22456:
				return 5060403;
				
			case 11286:
				return 56795890;
			case 11702:
				return 190000000;
			case 11704:
				return 90000000;
			case 11706:
				return 150000000;
			case 11708:
				return 110000000;
				
			case 11694:
				return 190000000;
			case 11696:
				return 90000000;
			case 11698:
				return 150000000;
			case 11700:
				return 110000000;
			case 14484:
				return 1500000000;
				
			case 13746:
				return 137000000;
			case 13748:
				return 493000000;
			case 13750:
				return 427000000;
			case 13752:
				return 87000000;
			case 13736:
				return 59000000;
			case 13738:
				return 196000000;
			case 13740:
				return 552000000;
			case 13742:
				return 486000000;
			case 13744:
				return 146000000;
			case 11335:
				return 78000000;
			case 14479:
				return 29000000;
			case 11730:
				return 14000000;
			case 6585:
				return 10000000;
			case 22494:
				return 210000000;
			case 22498:
				return 37000000;
			case 22482:
				return 350000000;
			case 22486:
				return 350000000;
			case 22490:
				return 350000000;
			case 22470:
				return 100000000;
			case 22474:
				return 100000000;
			case 22478:
				return 100000000;
			case 11718:
				return 100000000;
			case 11720:
				return 100000000;
			case 11722:
				return 100000000;
			case 11724:
				return 100000000;
			case 11726:
				return 100000000;
			case 11728:
				return 10000000;
			case 21787:
				return 157000000;
			case 21790:
				return 97470000;
			case 21793:
				return 74650000;
			case 21369:
				return 357372400;
				
				
				


                case 4708://barrows starts here
                        item.getDefinitions().setValue(10000000);//10m a pice
                        break;
                case 4710:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4712:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4714:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4716:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4718:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4720:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4722:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4724:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4726:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4728:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4730:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4732:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4734:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4736:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4738:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4745:
                        item.getDefinitions().setValue(10000000);
                        break;
                 case 9790:
                        item.getDefinitions().setValue(200000);
                        break;
	case 1044:
                        item.getDefinitions().setValue(1000000);
                        break;
	case 4747:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4749:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4751:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4753:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4755:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4757:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 4759:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 21736:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 21744:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 21752:
                        item.getDefinitions().setValue(10000000);
                        break;
                case 21760:
                        item.getDefinitions().setValue(10000000);
                        break;//barrows ends
                case 6685:
                        item.getDefinitions().setValue(10000);
                        break;
                case 11716:
                        item.getDefinitions().setValue(10000000);
                        break; // edge shop ends	
                case 15486://mage shop starts here
                        item.getDefinitions().setValue(5000000);
                        break;
				case 6916:
                        item.getDefinitions().setValue(5000000);
                        break;						
                case 6918:
                        item.getDefinitions().setValue(17000000);
                        break;
                case 6924:
                        item.getDefinitions().setValue(12000000);
                        break;
                case 6922:
                        item.getDefinitions().setValue(17000000);
                        break;
                case 6920:
                        item.getDefinitions().setValue(3000000);
                        break;
                case 6914:
                        item.getDefinitions().setValue(6000000);
                        break; // mage ends here	
                case 2497://range shop starts here
                        item.getDefinitions().setValue(2000);
                        break;
                case 2491:
                        item.getDefinitions().setValue(5000);
                        break;
                case 2581:
                        item.getDefinitions().setValue(2500000);
                        break;
                case 2577:
                        item.getDefinitions().setValue(30000000);
                        break;
                case 11235:
                        item.getDefinitions().setValue(5000000);
                        break;
                case 11212:
                        item.getDefinitions().setValue(500);
                        break;
                case 9341:
                        item.getDefinitions().setValue(300);
                        break;
                case 4212:
                        item.getDefinitions().setValue(200000);
                        break;
                case 10499:
                        item.getDefinitions().setValue(100000);
                        break;
                case 9144:
                        item.getDefinitions().setValue(800);
                        break;//range shop ends
				case 18830:
                        item.getDefinitions().setValue(80000);
						break;
				case 534:
                        item.getDefinitions().setValue(8000);
						break;
				case 532:
                        item.getDefinitions().setValue(4000);
						break;
						
				case 3124:
                        item.getDefinitions().setValue(40000);
						break;
			
			
			}
		return defs.getValue(); // TODO get price from real item from saved
									
	}

	private EconomyPrices() {

	}
}
