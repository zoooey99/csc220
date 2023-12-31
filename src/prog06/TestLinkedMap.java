package prog06;

public class TestLinkedMap extends LinkedMap {
    Entry a1;
    Entry b2;
    Entry c3;
    Entry d4;
    Entry e5;
    Entry f6;
    Entry x0;

    TestLinkedMap () {
		a1 = new Entry("a", "1");
		b2 = new Entry("b", "2");
		c3 = new Entry("c", "3");
		d4 = new Entry("d", "4");
		e5 = new Entry("e", "5");
		f6 = new Entry("f", "6");
		x0 = new Entry("x", "0");
    }

    void set (Entry[] entries) {
		if (entries.length == 0) {
			first = last = null;
			return;
		}
		first = entries[0];
		entries[0].previous = null;
		for (int i = 1; i < entries.length; i++) {
			entries[i-1].next = entries[i];
			entries[i].previous = entries[i-1];
		}
		last = entries[entries.length-1];
		entries[entries.length-1].next = null;
    }

    boolean equals (Entry[] entries) {
		if (first != entries[0])
			return false;
		if (entries[0].previous != null)
			return false;
		for (int i = 1; i < entries.length; i++) {
			if (entries[i-1].next != entries[i])
			return false;
			if (entries[i].previous != entries[i-1])
			return false;
		}
		if (last != entries[entries.length-1])
			return false;
		if (entries[entries.length-1].next != null)
			return false;
		return true;
    }

    boolean testFind (Entry[] in, String s, Entry out) {
	set(in);
	try {
	    Entry ret = find(s);
	    if (ret != out)
		return false;
	} catch (Exception e) {
	    return false;
	}
	return true;
    }

    boolean testFound (Entry[] in, Entry entry, String s, boolean out) {
		set(in);
		boolean ret = !out;
		try {
			ret = found(entry, s);
			if (ret != out)
			return false;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return true;
    }

    boolean testAdd (Entry[] in, Entry previous, Entry newEntry, Entry[] out) {
		newEntry.next = null;
		newEntry.previous = null;
		set(in);
		try {
			add(previous, newEntry);
			if (!equals(out))
			return false;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return true;
    }


    void check (boolean b) {
		if (!b)
			System.out.println("FAIL");
		else
			System.out.println("PASS");
    }

    void testFind () {
		{
			Entry[] in = {};
			check(testFind(in, "a", null));
		}
		{
			Entry[] in = {b2};
			check(testFind(in, "a", null));
			check(testFind(in, "b", b2));
			check(testFind(in, "c", b2));
		}
		{
			Entry[] in = {b2, d4};
			check(testFind(in, "a", null));
			check(testFind(in, "b", b2));
			check(testFind(in, "c", b2));
			check(testFind(in, "d", d4));
			check(testFind(in, "e", d4));
		}
		{
			Entry[] in = {b2, d4, f6};
			check(testFind(in, "a", null));
			check(testFind(in, "b", b2));
			check(testFind(in, "c", b2));
			check(testFind(in, "d", d4));
			check(testFind(in, "e", d4));
			check(testFind(in, "f", f6));
			check(testFind(in, "g", f6));
		}
    }

    void testFound () {
		{
			Entry[] in = {};
			check(testFound(in, null, "a", false));
		}
		{
			Entry[] in = {a1};
			check(testFound(in, null, "a", false));
			check(testFound(in, a1, "a", true));
			check(testFound(in, a1, "x", false));
		}
    }

    void testAdd () {
		{
			Entry[] in = {};
			Entry[] out = {x0};
			check(testAdd(in, null, x0, out));
		}
		{
			Entry[] in = {a1};
			Entry[] out = {x0, a1};
			check(testAdd(in, null, x0, out));
		}
		{
			Entry[] in = {a1};
			Entry[] out = {a1, x0};
			check(testAdd(in, a1, x0, out));
		}
		{
			Entry[] in = {a1, b2};
			Entry[] out = {x0, a1, b2};
			check(testAdd(in, null, x0, out));
		}
		{
			Entry[] in = {a1, b2};
			Entry[] out = {a1, x0, b2};
			check(testAdd(in, a1, x0, out));
		}
		{
			Entry[] in = {a1, b2};
			Entry[] out = {a1, b2, x0};
			check(testAdd(in, b2, x0, out));
		}
		{
			Entry[] in = {a1, b2, c3};
			Entry[] out = {x0, a1, b2, c3};
			check(testAdd(in, null, x0, out));
		}
		{
			Entry[] in = {a1, b2, c3};
			Entry[] out = {a1, x0, b2, c3};
			check(testAdd(in, a1, x0, out));
		}
		{
			Entry[] in = {a1, b2, c3};
			Entry[] out = {a1, b2, x0, c3};
			check(testAdd(in, b2, x0, out));
		}
		{
			Entry[] in = {a1, b2, c3};
			Entry[] out = {a1, b2, c3, x0};
			check(testAdd(in, c3, x0, out));
		}
    }
	boolean testRemove(Entry[] in, Entry entry, Entry[] out){
		set(in);
		try {
			remove(entry);
			if (!equals(out))
				return false;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return true;
	}
	void testRemove(){
		{ // removes middle
			Entry[] in = {a1, b2, c3};
			Entry[] out = {a1, c3};
			check(testRemove(in, b2, out));
		}
		{ //removes first
			Entry[] in = {a1, b2, c3};
			Entry[] out = {b2, c3};
			check(testRemove(in, a1, out));
		}
		{ //removes last
			Entry[] in = {a1, b2, c3};
			Entry[] out = {a1, b2};
			check(testRemove(in, c3, out));
		}
		{ //removes something not there
			Entry[] in = {x0};
			Entry[] out = {x0};
			check(testRemove(in, a1, out));
		}
	}

    public static void main (String[] args) {
		TestLinkedMap tlm = new TestLinkedMap();
		System.out.println("testing find");
		tlm.testFind();
		System.out.println("testing found");
		tlm.testFound();
		System.out.println("testing add");
		tlm.testAdd();
		System.out.println("testing remove");
		tlm.testRemove();

		}
}

    
