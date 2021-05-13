package testhsm1hsm2;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class HsmMonitorChangeHsm implements Runnable {

	private Boolean hsm1state = false;
	private Boolean hsm2state = false;
	private String state = "";

	@Override
	public void run() {

		hsm1set();
		try {
			state = getHsmStatus();
			if (state != null) {
				hsm1state = true;
				System.out.println("Hsm One connected start");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			state = null;
			hsm1state = false;
			e.printStackTrace();
		}

		if (!hsm1state && !hsm2state) {
			hsm2set();
			try {
				state = getHsmStatus();
				if (state != null) {
					hsm2state = true;
					System.out.println("Hsm two connected start");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				state = null;
				hsm2state = false;
				e.printStackTrace();
			}

		}

		while (true) {

			try {
				state = getHsmStatus();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				state = null;
				e.printStackTrace();
			}

			if (state == null) {

				if (hsm1state) {
					hsm1state = false;
					hsm2set();
					try {
						state = getHsmStatus();
						if (state != null) {
							hsm2state = true;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						state = null;
						hsm2state = false;
						e.printStackTrace();
					}
				}

			}

			try {
				state = getHsmStatus();
			} catch (Exception e) {
				state = null;
				e.printStackTrace();
			}

			if (state == null) {
				if (hsm2state) {
					hsm2state = false;
					hsm1set();
					try {
						state = getHsmStatus();
						if (state != null) {
							hsm1state = true;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						state = null;
						hsm1state = false;
						e.printStackTrace();
					}
				}
			}

			try {
				state = getHsmStatus();
			} catch (Exception e) {
				state = null;
				e.printStackTrace();
			}

			if (state == null) {

				if (!hsm1state && !hsm2state) {
					hsm1set();
					try {
						state = getHsmStatus();
						if (state != null) {
							hsm1state = true;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						state = null;
						hsm1state = false;
						e.printStackTrace();
					}

					if (!hsm1state && !hsm2state) {
						hsm2set();
						try {
							state = getHsmStatus();
							if (state != null) {
								hsm2state = true;
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							state = null;
							hsm2state = false;
							e.printStackTrace();
						}

					}
				}
			}

			try {
				if (hsm1state) {
					System.out.println("The Hsm One is Healthy ");
					Thread.sleep(2000);
				}
				if (hsm2state) {
					System.out.println("The Hsm two is Healthy ");
					Thread.sleep(2000);
				}
				if (!hsm1state && !hsm2state) {
					System.out.println("The Hsm one and two is Fails");
					Thread.sleep(2000);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

	}

	public synchronized static void hsm1set() {
		try {
			FileWriter myWriter = new FileWriter("/etc/default/et_hsm");
			myWriter.write("ET_HSM_NETCLIENT_SERVERLIST=192.168.20.121\r\n"
					+ "#ET_HSM_NETCLIENT_SERVERLIST=192.168.1.215\r\n #sahanbcs ");
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}

	public synchronized static void hsm2set() {
		try {
			FileWriter myWriter = new FileWriter("/etc/default/et_hsm");
			myWriter.write("#ET_HSM_NETCLIENT_SERVERLIST=192.168.20.121\r\n"
					+ "ET_HSM_NETCLIENT_SERVERLIST=192.168.1.215\r\n#sahanbcs");
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}

	public static String getHsmStatus() throws Exception {

		Runtime rt = null;
		Process pr = null;
		BufferedReader input = null;
		InputStreamReader inst = null;

		String usage = null;
		try {
			rt = Runtime.getRuntime();

			pr = rt.exec("hsmstate");

			inst = new InputStreamReader(pr.getInputStream());

			input = new BufferedReader(inst);

			String rs = "";

			if ((rs = input.readLine()) != null) {
//				System.out.println(rs);
				if (rs.contains("NORMAL MODE")) {

					int i = rs.indexOf("=");
					usage = rs.substring(i + 1);

				}

			}

			int exitVal = pr.waitFor();

			if (exitVal == 0) {

			}

			rs = null;
		} catch (Exception e) {

			throw e;

		} finally {
			if (input != null)
				input.close();
			if (inst != null)
				inst.close();
			if (pr != null)
				pr.destroy();

			input = null;
			inst = null;
			pr = null;
			rt = null;
		}

		return usage;

	}

}
