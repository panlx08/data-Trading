package hfut.hu.BlockValueShare.dh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class KeySuitController {
	protected static Logger logger= 
			LoggerFactory.getLogger(KeySuitController.class);
	
	@Autowired
	private KeySuitService keySuitService;
	public KeySuitVO change() {
		return keySuitService.getRandowKeySuit();
	}
	
}
