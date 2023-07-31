package firok.spring.slicerhandle;

import firok.topaz.spring.Ret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@RestController
public class ImageSaverController
{
    public record SaveImageRequest(
            String snapshot,
            Float startX,
            Float startY,
            Float width,
            Float height
    ) { }
    @Autowired
    SlicerHandleConfig config;

    @CrossOrigin(origins = "*")
    @PostMapping("/save-image")
    public Ret<Void> saveImage(
            @RequestBody List<SaveImageRequest> request
    )
    {
        var count = request.size();
        var folder = config.folderSaveImage;

        try
        {
            if(!folder.exists() && !folder.mkdirs()) return Ret.fail("无法创建目录");

            var files = folder.listFiles();
            final var countFiles = files == null ? 0 : files.length;

            IntStream.range(0, count).parallel().forEach(index -> {
                var imageRequest = request.get(index);
                try
                {
                    var b64string = imageRequest.snapshot;

                    var indexMid = b64string.indexOf(";base64,");
                    var imageFormat = b64string.substring(11, indexMid);
                    var imageBase64 = b64string.substring(indexMid + 8);

                    var imageData = Base64.getDecoder().decode(imageBase64);
                    var imageIs = new ByteArrayInputStream(imageData);
                    var imageObj = ImageIO.read(imageIs);

                    var startX = imageRequest.startX;
                    var startY = imageRequest.startY;
                    var width = imageRequest.width;
                    var height = imageRequest.height;

                    var imageCut = new BufferedImage(width.intValue(), height.intValue(), BufferedImage.TYPE_INT_ARGB);
                    var gra = imageCut.getGraphics();
                    gra.drawImage(
                            imageObj,
                            0, 0, width.intValue(), height.intValue(),
                            startX.intValue(), startY.intValue(), width.intValue() + startX.intValue(), height.intValue() + startY.intValue(),
                            (img, infoflags, x, y, width1, height1) -> true
                    );

                    var fileImage = new File(folder, config.getImageSavePrefix() + (countFiles + index + 1) + "-" + UUID.randomUUID() + "." + imageFormat);
                    ImageIO.write(imageCut, imageFormat, fileImage);

                    System.out.println("保存图像完成: " + fileImage);
                }
                catch (Exception any)
                {
                    throw new RuntimeException("无法保存图像", any);
                }
            });
            return Ret.success();
        }
        catch (Exception any)
        {
            return Ret.fail(any);
        }
    }
}
