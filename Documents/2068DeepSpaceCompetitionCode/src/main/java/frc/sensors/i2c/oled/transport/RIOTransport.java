package frc.sensors.i2c.oled.transport;

import edu.wpi.first.wpilibj.I2C;

/**
 * A {@link Transport} implementation that utilises I<sup>2</sup>C.
 *
 * @author fauxpark
 */
public class RIOTransport implements Transport {
	/**
	 * The Data/Command bit position.
	 */
	private static final int DC_BIT = 6;

	/**
	 * The internal GPIO instance.
	 */
	//private GpioController gpio;

	/**
	 * The GPIO pin corresponding to the RST line on the display.
	 */
	//private GpioPinDigitalOutput rstPin;

	/**
	 * The internal I<sup>2</sup>C device.
	 */
	//private I2CDevice i2c;

	private I2C bus;

	/**
	 * I2CTransport constructor.
	 *
	 * @param rstPin The GPIO pin to use for the RST line.
	 * @param bus The I<sup>2</sup>C bus to use.
	 * @param address The I<sup>2</sup>C address of the display.
	 */
	public RIOTransport(I2C bus) {
		this.bus = bus;
	}

	@Override
	public void reset() {
		/*try {
			rstPin.setState(true);
			Thread.sleep(1);
			rstPin.setState(false);
			Thread.sleep(10);
			rstPin.setState(true);
		} catch(InterruptedException e) {
			e.printStackTrace();
        }*/
	}

	@Override
	public void shutdown() {
		//gpio.shutdown();
	}

	@Override
	public void command(int command, int... params) {
		//byte[] commandBytes = new byte[params.length + 1];
		//commandBytes[0] = (byte) (0 << DC_BIT);
		//commandBytes[0] = (byte) command;

		bus.write(0 << DC_BIT, command);

		for(int i = 0; i < params.length; i++) {
			bus.write(0 << DC_BIT, params[i]);
		}

		/*try {
			i2c.write(commandBytes);
		} catch(IOException e) {
			e.printStackTrace();
		}*/
	}

	@Override
	public void data(byte[] data) {
		//byte[] dataBytes = new byte[data.length + 1];
		//dataBytes[0] = (byte) (1 << DC_BIT);

		for(int i = 0; i < data.length; i++) {
			bus.write((byte) 1 << DC_BIT, data[i]);
		}

		/*try {
			i2c.write(dataBytes);
		} catch(IOException e) {
			e.printStackTrace();
		}*/
	}
}
