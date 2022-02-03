package it.unibo.ewp.api.iia.get;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ConditionsHashCalculatorTest {

    @Test
    public void versionsBefore6SingleIia() throws IOException {
        InputStream xmlIs = ConditionsHashCalculatorTest.class.getClassLoader().getResourceAsStream("tests/iia/conditions-hash/v4/iias-get-response-single-iia.xml");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[1024];

        while ((nRead = xmlIs.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        xmlIs.close();

        ConditionsHashCalculator calculator = new ConditionsHashCalculatorVersionsBefore6();

        List<String> hashes = calculator.calculate(buffer.toByteArray());
        Assertions.assertNotNull(hashes);
        Assertions.assertEquals(1, hashes.size());
        Assertions.assertEquals("4263fc335205c12e735c91f1eb869e63015ed32e0bc2caafcf32556df3512fa2", hashes.get(0));
    }

    @Test
    public void versionsBefore6MultipleIia() throws IOException {
        InputStream xmlIs = ConditionsHashCalculatorTest.class.getClassLoader().getResourceAsStream("tests/iia/conditions-hash/v4/iias-get-response-multiple-iias.xml");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[1024];

        while ((nRead = xmlIs.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        xmlIs.close();

        ConditionsHashCalculator calculator = new ConditionsHashCalculatorVersionsBefore6();

        List<String> hashes = calculator.calculate(buffer.toByteArray());
        Assertions.assertNotNull(hashes);
        Assertions.assertEquals(2, hashes.size());
        Assertions.assertEquals("4263fc335205c12e735c91f1eb869e63015ed32e0bc2caafcf32556df3512fa2", hashes.get(0));
        Assertions.assertEquals("43621b808f6afea34b331836b2c4c546dd51fe536d9fa2e5ea4b509bae5a8ceb", hashes.get(1));
    }

    @Test
    public void version6SingleIia() throws IOException {
        InputStream xmlIs = ConditionsHashCalculatorTest.class.getClassLoader().getResourceAsStream("tests/iia/conditions-hash/v6/iias-get-response-single-iia.xml");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[1024];

        while ((nRead = xmlIs.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        xmlIs.close();

        ConditionsHashCalculator calculator = new ConditionsHashCalculatorVersion6();

        List<String> hashes = calculator.calculate(buffer.toByteArray());
        Assertions.assertNotNull(hashes);
        Assertions.assertEquals(1, hashes.size());
        Assertions.assertEquals("e9b092be55662bf463ce331271710afc3ec25354bc91dcb6ba1919297e746016", hashes.get(0));
    }

    @Test
    public void version6SingleIiaWithContacts() throws IOException {
        InputStream xmlIs = ConditionsHashCalculatorTest.class.getClassLoader().getResourceAsStream("tests/iia/conditions-hash/v6/iias-get-response-single-iia-with-contacts.xml");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[1024];

        while ((nRead = xmlIs.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        xmlIs.close();

        ConditionsHashCalculator calculator = new ConditionsHashCalculatorVersion6();

        List<String> hashes = calculator.calculate(buffer.toByteArray());
        Assertions.assertNotNull(hashes);
        Assertions.assertEquals(1, hashes.size());
        Assertions.assertEquals("e9b092be55662bf463ce331271710afc3ec25354bc91dcb6ba1919297e746016", hashes.get(0));
    }

    @Test
    public void version6MultipleIias() throws IOException {
        InputStream xmlIs = ConditionsHashCalculatorTest.class.getClassLoader().getResourceAsStream("tests/iia/conditions-hash/v6/iias-get-response-multiple-iias.xml");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[1024];

        while ((nRead = xmlIs.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        xmlIs.close();

        ConditionsHashCalculator calculator = new ConditionsHashCalculatorVersion6();

        List<String> hashes = calculator.calculate(buffer.toByteArray());
        Assertions.assertNotNull(hashes);
        Assertions.assertEquals(2, hashes.size());
        Assertions.assertEquals("e9b092be55662bf463ce331271710afc3ec25354bc91dcb6ba1919297e746016", hashes.get(0));
        Assertions.assertEquals("bde169c117ebada7d7e11d6c7f2f08b4ffe39fcb7fce96ca6b2de60427529c6a", hashes.get(1));
    }

}
