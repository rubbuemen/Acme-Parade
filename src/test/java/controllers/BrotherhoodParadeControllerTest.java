
package controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import controllers.brotherhood.BrotherhoodParadeController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class BrotherhoodParadeControllerTest extends AbstractTest {

	private MockMvc						mockMvc;

	@Autowired
	private BrotherhoodParadeController	brotherhoodParadeController;


	@Before
	public void beforeTest() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.brotherhoodParadeController).build();
	}

	/**
	 * @author Rubén Bueno
	 *         Caso de uso: listar "Parades" logeado como "Brotherhood"
	 */
	@Test
	public void findParadesByBrotherhoodLoggedPositiveTest() throws Exception {
		this.authenticate("brotherhood1");
		this.mockMvc.perform(MockMvcRequestBuilders.get("/parade/brotherhood/list")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("parade/list"));
		this.unauthenticate();
	}

	/**
	 * @author Rubén Bueno
	 *         Caso de uso: intentar listar "Parades" logeado como "Admin"
	 */
	@Test(expected = AssertionError.class)
	public void findParadesByBrotherhoodLoggedNegativeTest() throws Exception {
		this.authenticate("admin");
		this.mockMvc.perform(MockMvcRequestBuilders.get("/parade/brotherhood/list")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("parade/list"));
		this.unauthenticate();
	}

	/**
	 * @author Rubén Bueno
	 *         Caso de uso: editar un "Parade" logeado como "Brotherhood", siendo propietario de este
	 */
	@Test
	public void editParadeBrotherhoodPositiveTest() throws Exception {
		this.authenticate("brotherhood1");
		final int paradeId = this.getEntityId("parade1");
		this.mockMvc.perform(MockMvcRequestBuilders.get("/parade/brotherhood/edit?paradeId=" + paradeId)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("parade/edit"));
		this.unauthenticate();
	}
	/**
	 * @author Rubén Bueno
	 *         Caso de uso: intento de editar un "Parade" logeado como "Brotherhood", sin ser propietario de este
	 */
	@Test(expected = AssertionError.class)
	public void editParadeBrotherhoodNegativeTest() throws Exception {
		this.authenticate("brotherhood1");
		final int paradeId = this.getEntityId("parade3");
		this.mockMvc.perform(MockMvcRequestBuilders.get("/parade/brotherhood/edit?paradeId=" + paradeId)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("parade/edit"));
		this.unauthenticate();
	}

	/**
	 * @author Rubén Bueno
	 *         Caso de uso: eliminar un "Parade" logeado como "Brotherhood", siendo propietario de este
	 */
	@Test
	public void deleteParadeBrotherhoodPositiveTest() throws Exception {
		this.authenticate("brotherhood1");
		final int paradeId = this.getEntityId("parade1");
		this.mockMvc.perform(MockMvcRequestBuilders.get("/parade/brotherhood/delete?paradeId=" + paradeId)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("parade/edit"));
		this.unauthenticate();
	}
	/**
	 * @author Rubén Bueno
	 *         Caso de uso: intento de eliminar un "Parade" logeado como "Brotherhood", sin ser propietario de este
	 */
	@Test(expected = AssertionError.class)
	public void deleteParadeBrotherhoodNegativeTest() throws Exception {
		this.authenticate("brotherhood1");
		final int paradeId = this.getEntityId("parade3");
		this.mockMvc.perform(MockMvcRequestBuilders.get("/parade/brotherhood/delete?paradeId=" + paradeId)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("parade/list"));
		this.unauthenticate();
	}
}
