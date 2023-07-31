package firok.spring.slicerhandle;

import org.junit.jupiter.api.Test;

public class Base64CutTests
{
    @Test
    void test()
    {
        var b64 = "data:image/png;base64,123";
        var indexMid = b64.indexOf(";base64,");
        var type = b64.substring(11, indexMid);
        System.out.println(type);
        var data = b64.substring(indexMid + 8);
        System.out.println(data);
    }
}
