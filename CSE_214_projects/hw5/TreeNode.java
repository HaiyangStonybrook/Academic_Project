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
 * 
 * This class hold information of TreeNode, include keyword, left
 * reference,right reference
 *
 */
public class TreeNode {
	private String[] keywords;
	private TreeNode left;
	private TreeNode right;

	/**
	 * Constructor with no param
	 */
	public TreeNode() {
		keywords = null;
		left = null;
		right = null;
	}

	/**
	 * get the keywords of the node
	 * 
	 * @return a String[] value
	 */
	public String[] getKeywords() {
		return keywords;
	}

	/**
	 * set keywords of the node
	 * 
	 * @param keywords
	 *            String[] value
	 */
	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

	/**
	 * get the left reference value
	 * 
	 * @return a TreeNode value
	 */
	public TreeNode getLeft() {
		return left;
	}

	/**
	 * set the left reference
	 * 
	 * @param left,
	 *            a TreeNode value
	 */
	public void setLeft(TreeNode left) {
		this.left = left;
	}

	/**
	 * get the right reference value
	 * 
	 * @return a TreeNode value
	 */
	public TreeNode getRight() {
		return right;
	}

	/**
	 * set the right reference
	 * 
	 * @param right,
	 *            a TreeNode value
	 */
	public void setRight(TreeNode right) {
		this.right = right;
	}

	/**
	 * return true if the node is leaf
	 * 
	 * @return a boolean value
	 */
	public boolean isLeaf() {
		return (this.left == null || this.right == null);
	}

	@Override
	/**
	 * override toString() method, print keywords of the node
	 * 
	 * @return a String value
	 */
	public String toString() {
		String str = "";
		for (int i = 0; i < keywords.length; i++) {
			str += keywords[i];
		}
		return str;
	}

}
