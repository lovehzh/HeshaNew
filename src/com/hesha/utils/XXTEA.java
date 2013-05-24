package com.hesha.utils;

public final class XXTEA {

	private static final int delta = 0x9E3779B9;

	/**
	 * Decrypt data with key.
	 * 
	 * @param data
	 * @param key
	 * @return
	 */
	public static final byte[] decrypt(final byte[] data, final byte[] key) {
		if (data.length == 0) {
			return data;
		}
		return toByteArray(decrypt(toIntArray(data, false), toIntArray(key,
				false)), true);
	}

	/**
	 * Decrypt data with key.
	 * 
	 * @param v
	 * @param k
	 * @return
	 */
	private static final int[] decrypt(final int[] v, int[] k) {
		int n = v.length - 1;

		if (n < 1) {
			return v;
		}
		if (k.length < 4) {
			int[] key = new int[4];

			System.arraycopy(k, 0, key, 0, k.length);
			k = key;
		}
		int z = v[n], y = v[0], sum, e;
		int p, q = 6 + 52 / (n + 1);

		sum = q * delta;
		while (sum != 0) {
			e = sum >>> 2 & 3;
			for (p = n; p > 0; p--) {
				z = v[p - 1];
				y = v[p] -= MX(sum, y, z, p, e, k);
			}
			z = v[n];
			y = v[0] -= MX(sum, y, z, p, e, k);
			sum = sum - delta;
		}
		return v;
	}

	/**
	 * Encrypt data with key.
	 * 
	 * @param data
	 * @param key
	 * @return
	 */
	public static final byte[] encrypt(final byte[] data, final byte[] key) {
		// System.out.println("data=");
		// for (byte b : data) {
		// System.out.println(b);
		// }
		// System.out.println("key=");
		// for (byte k : key) {
		// System.out.println(k);
		// }
		if (data.length == 0) {
			return data;
		}
		return toByteArray(encrypt(toIntArray(data, true), toIntArray(key,
				false)), false);
	}

	/**
	 * Encrypt data with key.
	 * 
	 * @param v
	 * @param k
	 * @return
	 */
	private static final int[] encrypt(final int[] v, int[] k) {
		// System.out.println("v=");
		// for (int b : v) {
		// System.out.println(b);
		// }
		// System.out.println("k=");
		// for (int k1 : k) {
		// System.out.println(k1);
		// }

		int n = v.length - 1;

		if (n < 1) {
			return v;
		}
		if (k.length < 4) {
			int[] key = new int[4];

			System.arraycopy(k, 0, key, 0, k.length);
			k = key;
		}
		int z = v[n], y = v[0], sum = 0, e;
		int p, q = 6 + 52 / (n + 1);

		while (q-- > 0) {
			sum = sum + delta;
			e = sum >>> 2 & 3;
			for (p = 0; p < n; p++) {
				y = v[p + 1];
				z = v[p] += MX(sum, y, z, p, e, k);
			}
			y = v[0];
			z = v[n] += MX(sum, y, z, p, e, k);
		}
		// System.out.println("end v=");
		// for (int b : v) {
		// System.out.println(b);
		// }
		return v;
	}

	private static final int MX(final int sum, final int y, final int z,
			final int p, final int e, final int[] k) {
		return (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y)
				+ (k[p & 3 ^ e] ^ z);
	}

	/**
	 * Convert int array to byte array.
	 * 
	 * @param data
	 * @param includeLength
	 * @return
	 */
	private static final byte[] toByteArray(final int[] data,
			final boolean includeLength) {
		int n = data.length << 2;

		if (includeLength) {
			int m = data[data.length - 1];

			if (m > n) {
				return null;
			} else {
				n = m;
			}
		}
		byte[] result = new byte[n];

		for (int i = 0; i < n; i++) {
			result[i] = (byte) ((data[i >>> 2] >>> ((i & 3) << 3)) & 0xff);
		}
		return result;
	}

	/**
	 * Convert byte array to int array.
	 * 
	 * @param data
	 * @param includeLength
	 * @return
	 */
	private static final int[] toIntArray(final byte[] data,
			final boolean includeLength) {
		int n = (((data.length & 3) == 0) ? (data.length >>> 2)
				: ((data.length >>> 2) + 1));
		int[] result;

		if (includeLength) {
			result = new int[n + 1];
			result[n] = data.length;
		} else {
			result = new int[n];
		}
		n = data.length;
		for (int i = 0; i < n; i++) {
			result[i >>> 2] |= (0x000000ff & data[i]) << ((i & 3) << 3);
		}
		return result;
	}

	private XXTEA() {
	}
}
