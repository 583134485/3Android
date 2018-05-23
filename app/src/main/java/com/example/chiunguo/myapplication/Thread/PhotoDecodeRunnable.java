package com.example.chiunguo.myapplication.Thread;

class PhotoDecodeRunnable implements Runnable {

    /*
     * Defines the code to run for this task.
     */
    @Override
    public void run() {
        // Moves the current Thread into the background
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        /*
         * Stores the current Thread in the PhotoTask instance,
         * so that the instance
         * can interrupt the Thread.
         */
       // mPhotoTask.setImageDecodeThread(Thread.currentThread());

        System.out.println("currentThread"+Thread.currentThread().getName().toString());
    }

}
