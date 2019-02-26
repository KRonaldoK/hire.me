package com.challenge.urlshortener.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton to help convert between "base 10 and base 62",
 */

public final class BaseMapperConverter {

	public static final BaseMapperConverter INSTANCE = new BaseMapperConverter();

	private Logger logger;

	private static HashMap<Character, Integer> charToIndexTable;

	private static List<Character> indexToCharTable;

	private BaseMapperConverter() {

		logger = Logger.getLogger(BaseMapperConverter.class.getName());
		logger.log(Level.INFO, "Initializing converter...");
		initializeCharToIndexTable();
		initializeIndexToCharTable();

	}

	/*
	 * Mapeia um caracter da base 62 para um caracter na base 10
	 */
	private void initializeCharToIndexTable() {

		try {
			charToIndexTable = new HashMap<>();

			// 0<-a, 1<-b, ..., 25<-z, ..., 52<-0, 61<-9

			for (int i = 0; i < 26; ++i) {

				char c = 'a';

				c += i;

				charToIndexTable.put(c, i);

			}

			for (int i = 26; i < 52; ++i) {

				char c = 'A';

				c += (i - 26);

				charToIndexTable.put(c, i);

			}

			for (int i = 52; i < 62; ++i) {

				char c = '0';

				c += (i - 52);

				charToIndexTable.put(c, i);

			}

		} catch (Exception e) {
			// throw new EJBException(e);
			// TODO: remove this
		} finally {
		}
	}

	/*
	 * Mapeia um caracter na base 10 para caracter da base 62
	 */
	private void initializeIndexToCharTable() {

		// 0->a, 1->b, ..., 25->z, ..., 52->0, 61->9

		indexToCharTable = new ArrayList<>();

		for (int i = 0; i < 26; ++i) {

			char c = 'a';

			c += i;

			indexToCharTable.add(c);

		}

		for (int i = 26; i < 52; ++i) {

			char c = 'A';

			c += (i - 26);

			indexToCharTable.add(c);

		}

		for (int i = 52; i < 62; ++i) {

			char c = '0';

			c += (i - 52);

			indexToCharTable.add(c);

		}

	}

	/**
	 * Converte da base 10 para a 62, utilizada no encurtamento de urls sem custom
	 * alias
	 *
	 * @param id identificador na base 10
	 * @return identificador na base 62
	 */
	public String createUniqueID(Long id) {

		List<Integer> base62ID = convertBase10ToBase62ID(id);

		StringBuilder uniqueURLID = new StringBuilder();

		for (int digit : base62ID) {

			uniqueURLID.append(indexToCharTable.get(digit));

		}

		return uniqueURLID.toString();

	}

	/**
	 * Helper function
	 *
	 */
	private List<Integer> convertBase10ToBase62ID(Long id) {

		List<Integer> digits = new LinkedList<>();

		while (id > 0) {

			int remainder = (int) (id % 62);

			((LinkedList<Integer>) digits).addFirst(remainder);

			id /= 62;

		}

		return digits;

	}

	/**
	 * Converte da base 62 para a 10; se o alias e a url encurtada estão sendo
	 * persistidas não foi necessário utilizar para as urls encurtadas sem custom
	 * alias
	 *
	 * @param uniqueID identificador na base 62
	 * @return identificador na base 10
	 */
	public Long getDictionaryKeyFromUniqueID(String uniqueID) {

		List<Character> base62Number = new ArrayList<>();

		for (int i = 0; i < uniqueID.length(); ++i) {

			base62Number.add(uniqueID.charAt(i));

		}

		Long dictionaryKey = convertBase62ToBase10ID(base62Number);

		return dictionaryKey;

	}

	/**
	 * Helper function
	 *
	 */
	private Long convertBase62ToBase10ID(List<Character> ids) {

		long id = 0L;

		int exp = ids.size() - 1;

		for (int i = 0; i < ids.size(); ++i, --exp) {

			int base10 = charToIndexTable.get(ids.get(i));

			id += (base10 * Math.pow(62.0, exp));

		}

		return id;

	}

	public static BaseMapperConverter getInstance() {
		return INSTANCE;
	}

}
