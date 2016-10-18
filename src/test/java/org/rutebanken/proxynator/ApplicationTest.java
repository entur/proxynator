package org.rutebanken.proxynator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rutebanken.proxynator.service.LittleProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTest {
	@Autowired
	private LittleProxyService littleProxyService;


	@Test
	public void setReadAndDelete() {
	}


}
