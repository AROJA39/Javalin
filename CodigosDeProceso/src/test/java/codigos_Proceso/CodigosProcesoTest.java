package codigos_Proceso;

import static org.junit.Assert.*;

import org.junit.Test;

public class CodigosProcesoTest {

	@Test
	public void bientestCodigoProceso1() {
		CodigosProceso codigosProceso= new CodigosProceso();			
		String type=codigosProceso.codigoProceso("141000");
		assertEquals("Ajuste De Anticipos En Efectivo Cuenta De Ahorros", type);
	}

	@Test
	public void bientestCodigoProceso2() {
		CodigosProceso codigosProceso= new CodigosProceso();			
		String type=codigosProceso.codigoProceso("4020242O");
		assertEquals("Depósito De Cuenta Corriente A Cuenta Corriente-Internet-Original", type);
	}

	@Test
	public void bientestCodigoProceso3() {
		CodigosProceso codigosProceso= new CodigosProceso();			
		String type=codigosProceso.codigoProceso("4010142D");
		assertEquals("Depósito De Cuenta Ahorros A Cuenta Ahorros-Internet-Devolución", type);
	}

	@Test
	public void maltestCodigoProceso1() {
		CodigosProceso codigosProceso= new CodigosProceso();			
		String type=codigosProceso.codigoProceso("14100");
		assertEquals("Ajuste De Anticipos En Efectivo Cuenta De Ahorros", type);
	}

	@Test
	public void maltestCodigoProceso2() {
		CodigosProceso codigosProceso= new CodigosProceso();			
		String type=codigosProceso.codigoProceso("4020242D");
		assertEquals("Depósito De Cuenta Corriente A Cuenta Corriente-Internet-Original", type);
	}

	@Test
	public void maltestCodigoProceso3() {
		CodigosProceso codigosProceso= new CodigosProceso();			
		String type=codigosProceso.codigoProceso("4010142O");
		assertEquals("Depósito De Cuenta Ahorros A Cuenta Ahorros-Internet-Devolución", type);
	}
	@Test
	public void maltestCodigoProceso4() {
		CodigosProceso codigosProceso= new CodigosProceso();			
		String type=codigosProceso.codigoProceso("14100*");
		assertEquals("Ajuste De Anticipos En Efectivo Cuenta De Ahorros", type);
	}
}
