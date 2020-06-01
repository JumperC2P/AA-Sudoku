package solver;

/**
 * @author Chih-Hsuan Lee <s3714761>
 *
 */
public class DLXNode {
	
	DLXNode leftNode, rightNode, upNode, downNode;
	int row;
	int col;
	int value;
	HeaderNode headerNode;
	
	public DLXNode() {
		leftNode = rightNode = upNode = downNode = this;
	}
	
	public DLXNode(int row, int col, int value, HeaderNode headerNode) {
		this();
		this.row = row;
		this.col = col;
		this.value = value;
		this.headerNode = headerNode;
	}
	
	public void coverLR() {
		this.leftNode.rightNode = this.rightNode;
		this.rightNode.leftNode = this.leftNode;
	}
	
	public DLXNode setDown(DLXNode node) {
        assert (this.headerNode == node.headerNode);
        node.downNode = this.downNode;
        node.downNode.upNode = node;
        node.upNode = this;
        this.downNode = node;
        return node;
    }
	
	
	public DLXNode setUp(DLXNode node) {
		assert (this.headerNode == node.headerNode);
		node.upNode = this.upNode;
		node.upNode.downNode = node;
		node.downNode = this;
		this.upNode = node;
		return node;
	}
	
	public DLXNode setRight(DLXNode node) {
		node.rightNode = this.rightNode;
        node.rightNode.leftNode = node;
        node.leftNode = this;
        this.rightNode = node;
        return node;
	}
	
	
	public DLXNode setLeft(DLXNode node) {
		node.leftNode = this.leftNode;
		node.leftNode.rightNode = node;
		node.rightNode = this;
		this.leftNode = node;
		return node;
	}
	
	public void uncoverLR() {
		this.leftNode.rightNode = this;
		this.rightNode.leftNode = this;
	}
	
	public void coverUD() {
		this.upNode.downNode = this.downNode;
		this.downNode.upNode = this.upNode;
	}
	
	public void uncoverUD() {
		this.upNode.downNode = this;
		this.downNode.upNode = this;
	}
	
	public HeaderNode getHeaderNode() {
		return headerNode;
	}
	
	public Integer getValue() {
		return value;
	}
	
	public String toString() {
		return "cell: " + row + ", " + col + ", " + value;
	}
	
	
	
}
