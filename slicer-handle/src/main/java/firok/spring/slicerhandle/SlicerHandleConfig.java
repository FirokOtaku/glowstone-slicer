package firok.spring.slicerhandle;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Data
@Configuration
@ConfigurationProperties(prefix = "firok.spring.slicer-handle")
public class SlicerHandleConfig
{
    File folderSaveImage;
    String imageSavePrefix;
}
