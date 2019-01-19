package ca.djiang.junction.core.memory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

import ca.djiang.junction.core.JunMemory;

public class PagingMemory<Word extends Number> implements JunMemory<Word> {

    //
    // Configuration: Page Allocation

    private final int pageSize;
    private final IntFunction<JunMemory<Word>> pageAllocator;

    //
    // State: Backing Map

    // TODO: Should this be a Map or a List?
    private final Map<Integer, JunMemory<Word>> allocatedPages;

    public PagingMemory(int pageSize, IntFunction<JunMemory<Word>> pageAllocator) {
        if (pageAllocator == null)
            throw new NullPointerException("pageAllocator is null");

        this.pageSize = pageSize;
        this.pageAllocator = pageAllocator;

        allocatedPages = new HashMap<>();
    }

    private JunMemory<Word> allocatePage() {
        return pageAllocator.apply(pageSize);
    }

    private int getPageIndex(int addr) {
        return addr / pageSize;
    }

    private int getPageOffset(int addr) {
        return addr % pageSize;
    }

    private JunMemory<Word> ensurePage(int addr) {
        return allocatedPages.computeIfAbsent(getPageIndex(addr), pageIndex -> allocatePage());
    }

    @Override
    public Word loadWord(int addr) {
        return ensurePage(addr).loadWord(getPageOffset(addr));
    }

    @Override
    public void storeWord(int addr, Word value) {
        ensurePage(addr).storeWord(getPageOffset(addr), value);
    }

}
