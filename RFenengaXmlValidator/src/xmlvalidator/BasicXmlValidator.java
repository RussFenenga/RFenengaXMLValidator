package xmlvalidator;

import java.util.regex.*;

public class BasicXmlValidator implements XmlValidator {

	public BasicStringStack tags = new BasicStringStack();
	public BasicStringStack linenums = new BasicStringStack();
	public String[] results = new String[5];
	public String line;

	@Override
	public String[] validate(String xmlDocument) {

		String expr = "</?([^> ]+)[^>]*>";
		Pattern p = Pattern.compile(expr);

		// Get a matcher to process our XML string
		Matcher m = p.matcher(xmlDocument);

		// Find all of the matches for expr in text
		while (m.find()) {
			String s = m.group(); // the tag including the tag attributes
			String ss = m.group(1); // just the name of the tag without
									// attributes
			line = calcLineNum(xmlDocument, m.start()) + "";
			if (!canIgnore(s)) {
				if (isOpen(s)) {
					linenums.push(line);
					tags.push(ss);
				} else {
					if (tags.getCount() == 0) {
						results[0] = "Orphan closing tag";
						results[1] = ss;
						results[2] = line; // closing tag line num
						return results;
					}
					if (ss.equals(tags.peek(0))) { // closing tag equals opening
						linenums.pop(); // tag
						tags.pop();
					} else {
						results[0] = "Tag mismatch";
						results[1] = tags.peek(0);
						results[2] = linenums.peek(0);
						results[3] = ss;
						results[4] = line;
						return results;
					}
				}
			}
		}
		if (tags.getCount() != 0) {
			results[0] = "Unclosed tag at end";
			results[1] = tags.peek(0);
			results[2] = linenums.peek(0);
			return results;
		} else
			return results = null;
	}

	// end of the loop just functions
	public boolean canIgnore(String cur) {
		if ((cur.charAt(cur.length() - 2) == '/'))
			return true;
		else if ((Character.isLetter(cur.charAt(1)))) {
			return false;
		} else if (cur.charAt(1) == '/') {
			return false;
		} else {
			return true;
		}

	}

	public boolean isOpen(String cur) {
		return cur.charAt(1) != '/'; // Checks to see if the tag is opening or
										// closing
	}

	public int calcLineNum(String xmlDocument, int index) {
		int linenum = 1;
		for (int i = 0; i < index; i++) {
			if (xmlDocument.substring(i, i + 1).equals("\n")) {
				linenum++;
			}
		}
		return linenum;
	}
}
