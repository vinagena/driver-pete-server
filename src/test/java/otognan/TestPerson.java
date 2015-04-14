package otognan;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import otognan.Person;

public class TestPerson {

    @Test
    public void testName() {
        Person person = new Person("Pete");
        assertEquals(person.getName(), "Pete");
    }

}
