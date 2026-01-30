package code;

import java.util.Arrays;

public class RemoveDuplicatesInPlace {

    /**
     * Removes duplicates from a sorted integer array in-place.
     *
     * @param nums The integer array.
     * @return The new length of the array after removing duplicates.
     */
    public static int removeDuplicates(int[] nums) {
        // Handle edge cases: null or empty array.
        if (nums == null || nums.length == 0) {
            return 0;
        }

        // 1. Sort the array to bring duplicates together.
        // This has a time complexity of O(n log n).
        Arrays.sort(nums);

        // 2. Use a 'writeIndex' to track the position for the next unique element.
        // The first element is always unique, so we start from the second position.
        int writeIndex = 1;

        // 3. Iterate through the array starting from the second element.
        for (int readIndex = 1; readIndex < nums.length; readIndex++) {
            // If the current element is different from the previous one, it's unique.
            if (nums[readIndex] != nums[readIndex - 1]) {
                // Place the unique element at the writeIndex position.
                nums[writeIndex] = nums[readIndex];
                // Move the writeIndex to the next available slot.
                writeIndex++;
            }
        }

        // 'writeIndex' is now the new length of the array with unique elements.
        return writeIndex;
    }

    public static void main(String[] args) {
        int[] numbers = {1, 5, 2, 8, 5, 9, 2, 3, 3, 1, 8};
        System.out.println("Original array: " + Arrays.toString(numbers));

        int newLength = removeDuplicates(numbers);

        System.out.println("New length of array: " + newLength);

        // Create a new array to display only the unique elements for clarity.
        int[] uniqueArray = new int[newLength];
        System.arraycopy(numbers, 0, uniqueArray, 0, newLength);

        System.out.println("Array with duplicates removed: " + Arrays.toString(uniqueArray));
        System.out.println("Original array after modification: " + Arrays.toString(numbers));
    }
}
