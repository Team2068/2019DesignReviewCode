package frc.sensors.i2c.oled;

import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import frc.sensors.i2c.oled.transport.RIOTransport;
import frc.sensors.i2c.oled.font.*;

import java.util.HashSet;


public class RIOSSD extends SSD1306{

    private Set<String> canAddresses, i2cAddresses, driverStation;

    private static final Font DEFAULT_FONT = new Standard5x8();

    private static final String POSITIVE_MESSAGE = "ALL CLEAR: GOOD LUCK!", 
                                   ERROR_MESSAGE = "PLEASE ADDRESS ERRORS";

    private boolean errors = false;

    public RIOSSD() {
        this(128, 32);
    }

    public RIOSSD(int width, int height) {
        super(width, height, new RIOTransport(new I2C(I2C.Port.kOnboard, 0x3C)));
        canAddresses = new HashSet<>();

        if (!DriverStation.getInstance().isDSAttached()) {
            driverStation.add("UNAVAILABLE");
        }
        //clear();
        update();
        display();
    }

    private String formatData(String title, Set<String> badData) {
        int maxLength = getWidth() / DEFAULT_FONT.getColumns();

        String predicate = "";
        if (badData == null || badData.isEmpty()) {
            predicate = "CLEAR";
        } else {
            errors = true;
            predicate = badData.stream().collect(Collectors.joining(" "));
        }

        predicate = IntStream.range(predicate.length() + 1, (maxLength - title.length() - 4)).mapToObj(i -> " ").collect(Collectors.joining("")) + predicate;

        return title + ":" + predicate;
    }

    public void add(String mapName, String data) {
        Set<String> temp;
        switch(mapName) {
            case "I2C":
                temp = i2cAddresses;
            break;
            case "CAN":
                temp = canAddresses;
            break;
            default:
                return;
        }
        temp.add(data);
        update();
    }

    private void update() {
        //clear();
        Graphics g = new Graphics(this);
        g.text(0, 0, DEFAULT_FONT, formatData("CAN", canAddresses));
        g.text(0, 8, DEFAULT_FONT, formatData("I2C", i2cAddresses));
        g.text(0, 16, DEFAULT_FONT, formatData("DRIVER", driverStation));
        g.centerText(24, DEFAULT_FONT, (errors)? ERROR_MESSAGE : POSITIVE_MESSAGE);
        display();
    }

    public void setAuto() {
        setImage("./main/java/frc/sensors/i2c/oled/auto.bmp");
    }

    public void setDriver() {
        setImage("./main/java/frc/sensors/i2c/oled/driver.bmp");
    }

    private void setImage(String path) {
        //clear();
        Graphics g = new Graphics(this);

        File bmpFile = new File(path);
        try {
            BufferedImage image = ImageIO.read(bmpFile);
            g.image(image, 0, 0, getWidth(), getHeight());
        } catch (IOException e) {
            update();
        }
        display();
    }
}