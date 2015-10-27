package com.chialung.jclprofile.Module;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class ThreadCenter {
    private static int th_num = 1;
    private static int clear_limit = 15;
    private static HashMap<Integer, Thread> thread_pos = new HashMap<Integer, Thread>(30);

    public static int addThread(Runnable new_thread) {
        return addThread(1, new Thread(new_thread));
    }

    public static int addThread(Thread new_thread) {
        return addThread(1, new_thread);
    }

    public static int addThread(int type, Thread new_thread) {
        switch (type) {
            case 2:
                return addThread(new_thread, (byte) 3, true);
            case 3:
                return addThread(new_thread, (byte) 5, true);
            default:
                return addThread(new_thread, (byte) 1, true);
        }
    }

    public static int addThread(int type, Runnable new_thread) {
        return addThread(type, new Thread(new_thread));
    }

    public static int addThread(Runnable new_thread, byte priority, boolean isDaemon) {
        return addThread(new Thread(new_thread), priority, isDaemon);
    }

    public static int addThread(Thread new_thread, byte priority, boolean isDaemon) {
        int num = get_numCard();
        if (isDaemon) {
            new_thread.setDaemon(true);
        }
        new_thread.setPriority(priority);
        thread_pos.put(num, new_thread);
        if (new_thread.getState() == Thread.State.NEW) {
            new_thread.start();
        }
        if (thread_pos.size() > clear_limit) {
            clearThread();
        }
        return num;
    }

    public static int addThread(int numCard, Thread new_thread, byte priority, boolean isDaemon) {
        int num = numCard;
        if (isDaemon) {
            new_thread.setDaemon(true);
        }
        new_thread.setPriority(priority);
        thread_pos.put(num, new_thread);
        new_thread.start();
        try {
            if (thread_pos.size() > clear_limit) {
                clearThread();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    private static int get_numCard() {
        return get_numCard(1);
    }

    /*
     * type list:
     * 0 :循環執行的process
     * 1 :http request
     * 2 :work process
     * 3 :system process
     */
    public static int get_numCard(int type) {
        if (th_num > 32767) {
            th_num = 1;
        }
        switch (type) {
            case 2:
                return 65536 | th_num++;
            case 3:
                return 131072 | th_num++;
            case 1:
                return 32768 | th_num++;
            default:
                return th_num++;
        }
    }

    // 請參照get_numCard的內容來給TYPE num
    public static void stopTypeThread(int type) {
        synchronized (thread_pos) {
            Set<Entry<Integer, Thread>> s = thread_pos.entrySet();
            Iterator<Entry<Integer, Thread>> it = s.iterator();
            Thread uh;
            while (it.hasNext()) {
                Entry<Integer, Thread> m = (Entry<Integer, Thread>) it.next();
                if ((m.getKey() < 32768) || (m.getKey() & type) > 0) {
                    uh = (Thread) m.getValue();
                    if (!uh.isAlive()) {
                        it.remove();
                    } else {
                        uh.interrupt();
                    }
                }
            }
            uh = null;
        }
    }

    public static Thread getThread(int num) {
        return thread_pos.get(num);
    }

    public static void stopAllThread() {
        synchronized (thread_pos) {
            Set<Entry<Integer, Thread>> s = thread_pos.entrySet();
            Iterator<Entry<Integer, Thread>> it = s.iterator();
            Thread uh;
            while (it.hasNext()) {
                Entry<Integer, Thread> m = (Entry<Integer, Thread>) it.next();
                uh = (Thread) m.getValue();
                if (uh != null && uh.isAlive()) {
                    uh.interrupt();
                }
                it.remove();
            }
        }
    }

    public static boolean isThreadOver(int num) {
        if (thread_pos.get(num) != null) {
            if (thread_pos.get(num).getState() == Thread.State.TERMINATED) {
                return true;
            }
            if (thread_pos.get(num).getState() == Thread.State.NEW) {
                return true;
            }
        }
        return false;
    }

    public static boolean stopThread(int num) {
        if (thread_pos.get(num) != null) {
            if (thread_pos.get(num).isAlive()) {
                thread_pos.get(num).interrupt();
                return true;
            }
        }
        return false;
    }

    public static void clearThread() {
        synchronized (thread_pos) {
            Set<Entry<Integer, Thread>> s = thread_pos.entrySet();
            Iterator<Entry<Integer, Thread>> it = s.iterator();
            Thread uh;
            clear_limit = 15;
            while (it.hasNext()) {
                Entry<Integer, Thread> m = (Entry<Integer, Thread>) it.next();
                uh = (Thread) m.getValue();
                if (!uh.isAlive()) {
                    it.remove();
                } else {
                    clear_limit += 1;
                }
            }
            uh = null;
        }
    }
}
