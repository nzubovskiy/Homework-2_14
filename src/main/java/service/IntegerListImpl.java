package service;

import exception.InvalidIndexException;
import exception.NullItemException;

import java.util.Arrays;


public class IntegerListImpl implements IntegerList{

    private Integer[] storage;
    private int size;

    public IntegerListImpl() {
        storage = new Integer[10];
    }

    public IntegerListImpl(int initSize) {
        storage = new Integer[initSize];
    }

    @Override
    public Integer add(Integer item) {
        validateSize();
        validateItem(item);
        storage[size++] = item;
        return item;
    }

    @Override
    public Integer add(int index, Integer item) {
        validateSize();
        validateItem(item);
        validateIndex(index);

        if (index == size) {
            storage[size++] = item;
            return  item;
        }
        System.arraycopy(storage, index, storage, index + 1, size - index);
        storage[index] = item;
        size++;
        return item;
    }

    @Override
    public void grow() {
        Integer[] newStorage = new Integer[(int) (size * 1.5)];
        for (int i = 0; i < storage.length; i++) {
            newStorage[i] = storage[i];
        }
        storage = newStorage;
        size = (int) (size * 1.5);
    }

    @Override
    public Integer set(int index, Integer item) {
        validateIndex(index);
        validateItem(item);
        storage[index] = item;
        return item;
    }

    @Override
    public Integer remove(Integer item) {
        validateItem(item);
        int index = indexOf(item);

        return remove(index);
    }

    @Override
    public Integer remove(int index) {
        validateIndex(index);

        Integer item = storage[index];

        if (index == size) {
            storage[size--] = null;
            return item;
        }
        System.arraycopy(storage, index + 1, storage, index, size - index);

        size--;
        return item;
    }


    @Override
    public boolean contains(Integer item) {
        sort(storage);
        int min = 0;
        int max = storage.length - 1;
        while (min <= max) {
            int mid = (min + max) / 2;
            if (item == storage[mid]) {
                return true;
            }
            if (item < storage[mid]) {
                max = mid - 1;
            } else {
                min = mid + 1;
            }
        }
        return false;
    }

    @Override
    public int indexOf(Integer item) {
        for (int i = 0; i < size; i++) {
            if (storage[i].equals(item)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Integer item) {
        for (int i = size - 1; i > 0; i--) {
            if (storage[i].equals(item)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Integer get(int index) {
        validateIndex(index);
        return storage[index];
    }

    @Override
    public boolean equals(IntegerList otherList) {
        return Arrays.equals(this.toArray(), otherList.toArray());
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public Integer[] toArray() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    public void sort(Integer[] storage) {
        if (storage.length < 2) {
            return;
        }
        Integer mid = storage.length / 2;
        Integer[] left = new Integer[mid];
        int i1 = storage.length - mid;
        Integer[] right = new Integer[i1];

        for (int i = 0; i < left.length; i++) {
            left[i] = storage[i];
        }

        for (int i = 0; i < right.length; i++) {
            right[i] = storage[mid + i];
        }

        sort(left);
        sort(right);

        merge(storage, left, right);
    }

    private static void merge(Integer[] storage, Integer[] left, Integer[] right) {

        int mainP = 0;
        int leftP = 0;
        int rightP = 0;
        while (leftP < left.length && rightP < right.length) {
            if (left[leftP] <= right[rightP]) {
                storage[mainP++] = left[leftP++];
            } else {
                storage[mainP++] = right[rightP++];
            }
        }
        while (leftP < left.length) {
            storage[mainP++] = left[leftP++];
        }
        while (rightP < right.length) {
            storage[mainP++] = right[rightP++];
        }
    }


    private void validateItem(Integer item) {
        if (item == null) {
            throw new NullItemException();
        }
    }

    private void validateSize() {
        if (size == storage.length) {
            grow();
        }
    }

    private void validateIndex(int index) {
        if (index < 0 || index > size) {
            throw new InvalidIndexException();
        }
    }
}
