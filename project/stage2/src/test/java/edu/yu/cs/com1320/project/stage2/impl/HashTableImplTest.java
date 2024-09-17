package edu.yu.cs.com1320.project.stage2.impl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.yu.cs.com1320.project.impl.HashTableImpl;
import org.junit.jupiter.api.Test;
import edu.yu.cs.com1320.project.impl.*;

public class HashTableImplTest {
    HashTableImpl<String, String> test = new HashTableImpl<>();
    @Test
    public void putTest() {

        //putting a key that doesn't exist yet should return null
        assertEquals(null,test.put("KeyOne","ValueOne"));
        //putting a key that does exist should return that old keys value
        assertEquals("ValueOne",test.put("KeyOne","Dif"));

        assertEquals("Dif",test.put("KeyOne","D"));
        assertEquals("D", test.get("KeyOne"));

        assertEquals(true, test.containsKey("KeyOne"));

        //deleting should return "D"
        assertEquals("D",test.put("KeyOne",null));
        assertEquals(null,test.get("KeyOne"));
        assertEquals(false, test.containsKey("KeyOne"));
        //looking for key that doesnt exist. shoud b null
        assertEquals(null, test.get("ajdibsdubfs"));
       assertEquals(null, test.put("wRONG",null));
    }

    @Test
    public void TestPut() {


        assertEquals(0,test.size());
        test.put("Key1","k1");
       test.put("Key2","K2");
        test.put("Key3","K3");
        test.put("Key4","K4");
        test.put("Key5","K5");
        test.put("Key6","K6");

        test.put("Key7","K7");
        test.put("Key8","K8");
        test.put("Key9","K9");
        test.put("Key10","K10");
        test.put("Key11","K11");
        test.put("Key12","K12");

        assertEquals("K6", test.get("Key6"));
        assertEquals("K6", test.put("Key6","NEW"));
        test.put("Key6","NEW");
        assertEquals("NEW", test.get("Key6"));
        assertEquals("NEW", test.put("Key6","NEW x2"));
        assertEquals("NEW x2", test.get("Key6"));

    }







}
