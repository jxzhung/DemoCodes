package com.jzhung.test.arithmetic.sort;

/**
 * 桶排序 时间复杂度O(M+N),浪费空间对N个数据排序需要N+1个空间
 */
public class BucketSort {
    public static void main(String[] args) {
        int[] stuScores = {100, 88, 14, 65, 85, 32, 85, 95};

        //辅助数组
        int[] fullScore = new int[101];

        //记录分数出现的次数
        for (int i = 0; i < stuScores.length; i++) {
            int score = stuScores[i];
            fullScore[score] = fullScore[score] + 1;
        }

        for (int i = 0; i < fullScore.length; i++) {
            int scoreCoun = fullScore[i];
            for (int j = 0; j < scoreCoun; j++) {
                System.out.print(i+",");
            }
        }
    }
}
