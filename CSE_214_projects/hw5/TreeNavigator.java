package hw5;

/**
 * Haiyang Liu
 * ID: 111000906
 * HomeWork 5
 * CSE Recitation R04
 * Recitation TA's Name: Jun Young Kim 
 * Grading TA's Name: Xiao Liang
 */

/**
 * This class build a tree structure, include buildTree(), classify(),
 * getPath(), etc
 * 
 */
public class TreeNavigator {
	private TreeNode root;
	private TreeNode cursor;

	/**
	 * Contructor with no param
	 */
	public TreeNavigator() {
		root = new TreeNode();
		cursor = new TreeNode();
	}

	/**
	 * get the root of the tree
	 * 
	 * @return a TreeNode value
	 */
	public TreeNode getRoot() {
		return root;
	}

	/**
	 * set the root of the tree
	 * 
	 * @param root, a TreeNode value
	 */
	public void setRoot(TreeNode root) {
		this.root = root;
	}

	/**
	 * get the cursor of the tree
	 * 
	 * @return a TreeNode value
	 * @throws NullPointerException,if cursor is null;
	 */
	public TreeNode getCursor() throws NullPointerException {
		if (cursor == null)
			throw new NullPointerException();
		return cursor;
	}

	/**
	 * set the cursor of the tree
	 * 
	 * @param cursor, a TreeNode value
	 */
	public void setCursor(TreeNode cursor) {
		this.cursor = cursor;
	}

	/**
	 * build tree from imported text file
	 * 
	 * @param treeFile a String value
	 * @return a TreeNavigator value
	 */
	public static TreeNavigator buildTree(String treeFile) throws IllegalFileException {
		if(treeFile==null)
			throw new IllegalFileException(); 
		TreeNavigator bt = new TreeNavigator();
		String[] lines = null;
		lines = treeFile.split("(?<=leaf)");// lines= each line
		String[] rootKeywords = null;
		String[] rootKeyword = null;
		rootKeywords = lines[0].split(";");
		rootKeyword = rootKeywords[1].split("(?<=,)");
		bt.root.setKeywords(rootKeyword);

		for (int i = 1; i < lines.length; i++) {
			if(!lines[i].contains(";"))
				throw new IllegalFileException();
			String[] keyword = null;
			keyword = lines[i].split(";");// keyword=separate by;
			bt.cursor = bt.root;
			for (int j = 1; j < keyword[0].length(); j++) {
				if (keyword[0].charAt(j) == '0' && j != keyword[0].length() - 1) {
					bt.cursorLeft();
				}
				if (keyword[0].charAt(j) == '0' && j == keyword[0].length() - 1) {
					TreeNode node = new TreeNode();
					String[] key = null;
					key = keyword[1].split("(?<=,)");
					node.setKeywords(key);
					bt.cursor.setLeft(node);
					bt.cursor = bt.cursor.getLeft();
					;
				}
				if (keyword[0].charAt(j) == '1' && j != keyword[0].length() - 1) {
					bt.cursor = bt.cursor.getRight();
				}
				if (keyword[0].charAt(j) == '1' && j == keyword[0].length() - 1) {
					TreeNode node = new TreeNode();
					String[] key = null;
					key = keyword[1].split("(?<=,)");
					node.setKeywords(key);
					bt.cursor.setRight(node);
					bt.cursor = bt.cursor.getRight();
				}
			}
		}
		bt.resetCursor();
		return bt;
	}

	/**
	 * Classifies the text with the given tree and returns the classification asa String.
	 * @param text,a String value
	 * @return a String value
	 */
	public String classify(String text) {
		TreeNode ptr = new TreeNode();
		ptr = root;
		boolean move;
		String record = "";
		String cur = "";
		while (!ptr.isLeaf()) {
			move = false;
			cur = ptr.toString();
			String[] cu = cur.split(",");
			for (int i = 0; i < cu.length; i++) {
				if (text.toLowerCase().contains(cu[i].toLowerCase())) {
					move = true;
				}
			}
			if (move == true) {
				ptr = ptr.getRight();
				record += "1";
			}
			if (move == false) {
				ptr = ptr.getLeft();
				record += "0";
			}
		}
		return ptr.toString();
	}

	/**
	 * get a numerical path from root to target, eg: 0101
	 * @param text a String value
	 * @return a String value
	 */
	public String getCursorPath(String text) {
		TreeNode ptr = new TreeNode();
		ptr = root;
		boolean move;
		String record = "";
		String cur = "";
		while (!ptr.isLeaf()) {
			move = false;
			cur = ptr.toString();
			String[] cu = cur.split(",");
			for (int i = 0; i < cu.length; i++) {
				if (text.toLowerCase().contains(cu[i].toLowerCase())) {
					move = true;
				}
			}
			if (move == true) {
				ptr = ptr.getRight();
				record += "1";
			}
			if (move == false) {
				ptr = ptr.getLeft();
				record += "0";
			}
		}
		return record;
	}

	/**
	 * get the path from user text input. eg: ¡°NOT red, NOT coyote,wolf, IS cat,IS orange, DECISION: Garfield¡±
	 * @param string, a String value
	 * @return a String value
	 */
	public String getPath(String string) {
		String str = "";
		String tree = "";
		String path = "";
		this.resetCursor();
		if (string.length() == 1)
			System.out.println("don't know");
		else
			for (int i = 0; i < string.length(); i++) {
				if (string.charAt(i) == '0') {
					path += "NO " + cursor.toString() + ", ";
					this.cursorLeft();
				}
				if (string.charAt(i) == '1') {
					path += "IS " + cursor.toString() + ", ";
					this.cursorRight();
				}
			}
		path = path + "DECISION: " + cursor.toString();
		return path;
	}

	/**
	 * reset the cursor to root
	 */
	public void resetCursor() {
		cursor = root;
	}

	/**
	 * move the cursor to its left reference
	 * @throws NullPointerException if the left is null
	 */
	public void cursorLeft() throws NullPointerException {
		if (cursor.getLeft() == null)
			throw new NullPointerException();
		cursor = cursor.getLeft();
	}

	/**
	 * move the cursor to its right reference
	 * @throws NullPointerException if the right is null
	 */
	public void cursorRight() throws NullPointerException {
		if (cursor.getRight() == null)
			throw new NullPointerException();
		cursor = cursor.getRight();
	}

	/**
	 * edit current cursor's keywords with user text input
	 * @param text a String value
	 */
	public void editCursor(String text) {
		String[] keyword = null;
		keyword = text.split("(?<=,)");
		cursor.setKeywords(keyword);
	}

	/**
	 * edit cursor's left keyword with user text input
	 * @param text, a String value
	 */
	public void editLeft(String text) {
		TreeNode ptr = new TreeNode();
		String[] keyword = null;
		keyword = text.split("(?<=,)");
		ptr.setKeywords(keyword);
		cursor.setLeft(ptr);
	}

	/**
	 * edit cursor's right keyword with user text input
	 * @param text, a String value
	 */
	public void editRight(String text) {
		TreeNode ptr = new TreeNode();
		String[] keyword = null;
		keyword = text.split("(?<=,)");
		ptr.setKeywords(keyword);
		cursor.setRight(ptr);
	}

	/**
	 * find the parent of current cursor's keywords
	 * @return a String value
	 */
	public String findCursorParent() {
		return findParent(root).toString();
	}

	/**
	 * a helper method to find parent
	 * 
	 * @param ptr a TreeNode value
	 * @return a TreeNode value
	 * @throws NullPointerException if cursor is at root, no parent
	 */
	private TreeNode findParent(TreeNode ptr) throws NullPointerException {
		if (cursor == root || root.getKeywords() == null) {
			throw new NullPointerException();
		}
		if (ptr.getLeft() == cursor || ptr.getRight() == cursor) {
			return ptr;
		} else {
			if (ptr.getLeft() != null) {
				ptr = findParent(ptr.getLeft());
			}
			if (ptr.getRight() != null) {
				ptr = findParent(ptr.getRight());
			}
			if (findParent(ptr.getLeft()) == null && findParent(ptr.getRight()) == null)
				ptr = findParent(ptr);
		}
		return ptr;
	}
}
