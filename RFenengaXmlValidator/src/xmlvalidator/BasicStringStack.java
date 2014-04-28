package xmlvalidator;

public class BasicStringStack implements StringStack {

	protected int count;
	protected int arraySize;
	protected int arraySizeIncr;
	protected String[] items;

	public BasicStringStack() {
		count = 0;
		arraySize = 20;
		arraySizeIncr = 10;
		items = new String[arraySize];
	}

	@Override
	public void push(String item) {
		if (count == arraySize) {
			arraySize += arraySizeIncr;
			String[] tempItems = new String[arraySize];
			for (int i = 0; i < count; i++)
				tempItems[i] = items[i];
			items = tempItems;
		}
		items[count++] = item;
	}

	@Override
	public String pop() {
		if (count == 0)
			return null;
		else {
			String item = items[count - 1];
			items[--count] = null;
			return item;
		}
	}

	@Override
	public String peek(int position) {
		if (count == 0)
			return null;
		else
			return items[(count - 1) - position];
	}

	@Override
	public int getCount() {
		return count;
	}

}
