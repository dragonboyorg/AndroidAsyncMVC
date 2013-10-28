package org.dragonboy.util;

/**
 * @author dragonboyorg 2013-8-16
 */
public class SparseIntSet {
	private int[] mValues;
	private int mSize;

	/**
	 * Creates a new SparseIntSet containing no listings.
	 */
	public SparseIntSet() {
		this(10);
	}

	/**
	 * Creates a new SparseIntSet containing no listings that will not require
	 * any additional memory allocation to store the specified number of
	 * listings.
	 */
	public SparseIntSet(int initialCapacity) {
		initialCapacity = ArrayUtils.idealIntArraySize(initialCapacity);

		mValues = new int[initialCapacity];
		mSize = 0;
	}

	@Override
	public SparseIntSet clone() {
		SparseIntSet clone = null;
		try {
			clone = (SparseIntSet) super.clone();
			clone.mValues = mValues.clone();
		} catch (CloneNotSupportedException cnse) {
			/* ignore */
		}
		return clone;
	}

	public int[] getBackingArray() {
		return mValues;
	}

	/**
	 * Gets the int listed from the specified key, or <code>0</code> if no such
	 * listing has been made.
	 */
	public int get(int index) {
		if (index > mSize || index < 0) {
			throwIndexOutOfBoundsException(index, mSize);
		}
		return mValues[index];
	}

	/**
	 * Removes the listing at the given index.
	 */
	public void removeAt(int index) {
		System.arraycopy(mValues, index + 1, mValues, index, mSize
				- (index + 1));
		mSize--;
	}

	public void remove(int value) {
		int i = binarySearch(mValues, 0, mSize, value);
		if (i >= 0) {
			removeAt(i);
		}
	}

	public void add(int value) {
		int i = binarySearch(mValues, 0, mSize, value);

		if (i >= 0) {
			return;
		} else {
			i = ~i;

			if (mSize >= mValues.length) {
				int n = ArrayUtils.idealIntArraySize(mSize + 1);

				int[] nvalues = new int[n];

				// Log.e("SparseIntSet", "grow " + mKeys.length + " to " + n);
				System.arraycopy(mValues, 0, nvalues, 0, mValues.length);

				mValues = nvalues;
			}

			if (mSize - i != 0) {
				// Log.e("SparseIntSet", "move " + (mSize - i));
				System.arraycopy(mValues, i, mValues, i + 1, mSize - i);
			}
			mValues[i] = value;
			mSize++;
		}
	}

	/**
	 * Puts a value into the array, optimizing for the case where the value is
	 * greater than all existing values in the array.
	 */
	public void append(int value) {
		if (mSize != 0 && value <= mValues[mSize - 1]) {
			add(value);
			return;
		}

		int pos = mSize;
		if (pos >= mValues.length) {
			int n = ArrayUtils.idealIntArraySize(pos + 1);

			int[] nvalues = new int[n];

			// Log.e("SparseIntSet", "grow " + mKeys.length + " to " + n);
			System.arraycopy(mValues, 0, nvalues, 0, mValues.length);

			mValues = nvalues;
		}

		mValues[pos] = value;
		mSize = pos + 1;
	}

	/**
	 * Returns the number of value listings that this SparseIntSet currently
	 * stores.
	 */
	public int size() {
		return mSize;
	}

	public boolean contains(int value) {
		int i = binarySearch(mValues, 0, mSize, value);
		return i >= 0;
	}

	/**
	 * Returns an index for which {@link #valueAt} would return the specified
	 * key, or a negative number if no keys map to the specified value. Beware
	 * that this is a linear search, unlike lookups by key, and that multiple
	 * keys can map to the same value and this will find only one of them.
	 */
	public int indexOf(int value) {
		return binarySearch(mValues, 0, mSize, value);
	}

	/**
	 * Removes all value listings from this SparseIntSet.
	 */
	public void clear() {
		mSize = 0;
	}
	
	public int[] toArray() {
		int s = mSize;
		int[] result = new int[s];
		System.arraycopy(mValues, 0, result, 0, s);
		return result;
	}

	private static int binarySearch(int[] a, int start, int len, int value) {
		int high = start + len, low = start - 1, guess;

		while (high - low > 1) {
			guess = (high + low) / 2;

			if (a[guess] < value)
				low = guess;
			else
				high = guess;
		}

		if (high == start + len)
			return ~(start + len);
		else if (a[high] == value)
			return high;
		else
			return ~high;
	}

	private static IndexOutOfBoundsException throwIndexOutOfBoundsException(
			int index, int size) {
		throw new IndexOutOfBoundsException("Invalid index " + index
				+ ", size is " + size);
	}
}
