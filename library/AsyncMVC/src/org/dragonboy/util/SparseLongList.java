package org.dragonboy.util;

/**
 * @author dragonboyorg 2013-8-16
 */
public class SparseLongList {
	private long[] mValues;
	private int mSize;

	/**
	 * Creates a new SparseLongList containing no listings.
	 */
	public SparseLongList() {
		this(10);
	}

	/**
	 * Creates a new SparseLongList containing no listings that will not require
	 * any additional memory allocation to store the specified number of
	 * listings.
	 */
	public SparseLongList(int initialCapacity) {
		initialCapacity = ArrayUtils.idealLongArraySize(initialCapacity);

		mValues = new long[initialCapacity];
		mSize = 0;
	}

	@Override
	public SparseLongList clone() {
		SparseLongList clone = null;
		try {
			clone = (SparseLongList) super.clone();
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

	public long set(int index, long value) {
		long[] a = mValues;
		if (index >= mSize) {
			throwIndexOutOfBoundsException(index, mSize);
		}
		long result = a[index];
		a[index] = value;
		return result;
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
		int i = indexOf(value);
		if (i >= 0) {
			removeAt(i);
		}
	}

	public void add(long value) {
		long[] a = mValues;
		int s = mSize;
		if (s == a.length) {
			int n = ArrayUtils.idealLongArraySize(s + 1);
			long[] newArray = new long[n];
			System.arraycopy(a, 0, newArray, 0, s);
			mValues = a = newArray;
		}
		a[s] = value;
		mSize = s + 1;
	}

	/**
	 * Returns the number of value listings that this SparseLongList currently
	 * stores.
	 */
	public int size() {
		return mSize;
	}

	public boolean contains(long value) {
		return 0 <= indexOf(value);
	}

	/**
	 * Returns an index for which {@link #valueAt} would return the specified
	 * key, or a negative number if no keys map to the specified value. Beware
	 * that this is a linear search, unlike lookups by key, and that multiple
	 * keys can map to the same value and this will find only one of them.
	 */
	public int indexOf(long value) {
		for (int i = 0; i < mSize; i++)
			if (mValues[i] == value)
				return i;

		return -1;
	}

	public int lastIndexOf(long value) {
		for (int i = mSize - 1; i >= 0; i--)
			if (mValues[i] == value)
				return i;

		return -1;
	}

	/**
	 * Removes all value listings from this SparseLongList.
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

	private static IndexOutOfBoundsException throwIndexOutOfBoundsException(
			int index, int size) {
		throw new IndexOutOfBoundsException("Invalid index " + index
				+ ", size is " + size);
	}

}
