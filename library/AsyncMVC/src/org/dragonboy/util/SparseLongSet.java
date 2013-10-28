package org.dragonboy.util;

/**
 * @author dragonboyorg 2013-8-16
 */
public class SparseLongSet {
	private long[] mValues;
	private int mSize;

	/**
	 * Creates a new SparseLongSet containing no listings.
	 */
	public SparseLongSet() {
		this(10);
	}

	/**
	 * Creates a new SparseLongSet containing no listings that will not require
	 * any additional memory allocation to store the specified number of
	 * listings.
	 */
	public SparseLongSet(int initialCapacity) {
		initialCapacity = ArrayUtils.idealLongArraySize(initialCapacity);

		mValues = new long[initialCapacity];
		mSize = 0;
	}

	@Override
	public SparseLongSet clone() {
		SparseLongSet clone = null;
		try {
			clone = (SparseLongSet) super.clone();
			clone.mValues = mValues.clone();
		} catch (CloneNotSupportedException cnse) {
			/* ignore */
		}
		return clone;
	}

	public long[] getBackingArray() {
		return mValues;
	}

	/**
	 * Gets the int listed from the specified key, or <code>0</code> if no such
	 * listing has been made.
	 */
	public long get(int index) {
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

	public void remove(long value) {
		int i = binarySearch(mValues, 0, mSize, value);
		if (i >= 0) {
			removeAt(i);
		}
	}

	public void add(long value) {
		int i = binarySearch(mValues, 0, mSize, value);

		if (i >= 0) {
			return;
		} else {
			i = ~i;

			if (mSize >= mValues.length) {
				int n = ArrayUtils.idealLongArraySize(mSize + 1);

				long[] nvalues = new long[n];

				// Log.e("SparseLongSet", "grow " + mKeys.length + " to " + n);
				System.arraycopy(mValues, 0, nvalues, 0, mValues.length);

				mValues = nvalues;
			}

			if (mSize - i != 0) {
				// Log.e("SparseLongSet", "move " + (mSize - i));
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
	public void append(long value) {
		if (mSize != 0 && value <= mValues[mSize - 1]) {
			add(value);
			return;
		}

		int pos = mSize;
		if (pos >= mValues.length) {
			int n = ArrayUtils.idealLongArraySize(pos + 1);

			long[] nvalues = new long[n];

			// Log.e("SparseLongSet", "grow " + mKeys.length + " to " + n);
			System.arraycopy(mValues, 0, nvalues, 0, mValues.length);

			mValues = nvalues;
		}

		mValues[pos] = value;
		mSize = pos + 1;
	}

	/**
	 * Returns the number of value listings that this SparseLongSet currently
	 * stores.
	 */
	public int size() {
		return mSize;
	}

	public boolean contains(long value) {
		int i = binarySearch(mValues, 0, mSize, value);
		return i >= 0;
	}

	/**
	 * Returns an index for which {@link #valueAt} would return the specified
	 * key, or a negative number if no keys map to the specified value. Beware
	 * that this is a linear search, unlike lookups by key, and that multiple
	 * keys can map to the same value and this will find only one of them.
	 */
	public int indexOf(long value) {
		return binarySearch(mValues, 0, mSize, value);
	}

	/**
	 * Removes all value listings from this SparseLongSet.
	 */
	public void clear() {
		mSize = 0;
	}
	
	public long[] toArray() {
		int s = mSize;
		long[] result = new long[s];
		System.arraycopy(mValues, 0, result, 0, s);
		return result;
	}

	private static int binarySearch(long[] a, int start, int len, long value) {
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
