package solver;

import java.util.List;

/**
 * The header column node of dancing link algorithm
 * 
 * @author Chih-Hsuan Lee <s3714761>
 *
 */
public class HeaderNode extends DLXNode {
	
	Integer index = null;
	int size = 0;
	
	public HeaderNode() {
		
	}
	
	public HeaderNode(Integer index) {
		super();
		this.index = index;
		headerNode = this;
	}
	
	
	/**
	 * The cover nodes with one nodes skipped.
	 * The method should only be called when solving killer sudoku with cage constraints
	 * @param coverCageNodes a list of recording which nodes are covered.
	 * @param skipNode the node wants to be skipped
	 * @return a list of recording which nodes are covered.
	 */
	public List<DLXNode> coverNodesWithSkipped(List<DLXNode> coverCageNodes, DLXNode skipNode) {
		return this.coverDownNodesWithSkipped(coverCageNodes, skipNode, this.downNode);
	}
	
	
	/**
	 * To cover the down nodes based on the skipped node until it face header node.
	 * @param coverCageNodes a list of recording which nodes are covered.
	 * @param skipNode the node wants to be skipped
	 * @param downNode the down node of indicated node
	 * @return
	 */
	private List<DLXNode> coverDownNodesWithSkipped(List<DLXNode> coverCageNodes, DLXNode skipNode, DLXNode downNode) {
		
		// Once the downNode is headerNode, end to method
		if (downNode == this.headerNode) {
			return coverCageNodes;
		
		// if the downNode is skipNode, skip the covering.
		}else if (downNode == skipNode){
			return this.coverDownNodesWithSkipped(coverCageNodes, skipNode, downNode.downNode);
		}else {
			// cover the node
			downNode.coverUD();
			if (downNode != downNode.headerNode)
				downNode.headerNode.size--;
			coverCageNodes.add(downNode);
			// cover all the right nodes of the downNode
			this.coverRightNode(downNode, downNode.rightNode);
			return this.coverDownNodesWithSkipped(coverCageNodes, skipNode, downNode.downNode);
		}
	}
	
	/**
	 * Uncover the nodes.
	 * The method should only be called when solving killer sudoku with cage constraints
	 * 
	 * @param baseNode the node wanted to be uncovered.
	 */
	public void uncoverNodesOnNode(DLXNode baseNode) {
		this.uncoverUpNodesOnNode(baseNode, baseNode.downNode);
	}

	
	/**
	 * Uncover the nodes, and update the size of related header node.
	 * @param baseNode
	 * @param endNode
	 */
	private void uncoverUpNodesOnNode(DLXNode baseNode, DLXNode endNode) {
		if (baseNode == endNode) {
			return;
		}else {
			baseNode.uncoverUD();
			if (baseNode != baseNode.headerNode)
				baseNode.headerNode.size++;
			this.uncoverLeftNode(baseNode, baseNode.leftNode);
		}
		
	}

	/**
	 * cover nodes
	 */
	public void coverNodes() {
		
		// cover "column" which is header column itself
		this.coverLR();
		// cover the nodes which connect with the header
		this.coverDownNodes(this.downNode);
	}

	
	/**
	 * cover all the down nodes and their right nodes
	 * @param downNode
	 */
	private void coverDownNodes(DLXNode downNode) {
		
		if (downNode == this.headerNode) {
			return;
		}else {
			this.coverRightNode(downNode, downNode.rightNode);
			this.coverDownNodes(downNode.downNode);
		}
	}
	
	
	/**
	 * cover the right nodes
	 * @param downNode the end node of the process
	 * @param rightNode the right node to cover
	 */
	private void coverRightNode(DLXNode downNode, DLXNode rightNode) {
		if (rightNode == downNode) {
			return;
		}else {
			rightNode.coverUD();
			rightNode.headerNode.size--;
			this.coverRightNode(downNode, rightNode.rightNode);
		}
	}

	
	/**
	 * uncover nodes
	 */
	public void uncoverNodes() {
		this.uncoverUpNodes(this.upNode);
		this.uncoverLR();
	}

	
	/**
	 * uncover all the up nodes and their left nodes
	 * @param upNode
	 */
	private void uncoverUpNodes(DLXNode upNode) {
		if (upNode == this.headerNode) {
			return;
		}else {
			this.uncoverLeftNode(upNode, upNode.leftNode);
			this.uncoverUpNodes(upNode.upNode);
		}
		
	}

	
	/**
	 * uncover the left nodes
	 * @param upNode the end node of the process
	 * @param leftNode the left node to cover
	 */
	private void uncoverLeftNode(DLXNode upNode, DLXNode leftNode) {
		if (leftNode == upNode) {
			return;
		}else {
			leftNode.uncoverUD();
			leftNode.headerNode.size++;
			this.uncoverLeftNode(upNode, leftNode.leftNode);
		}
		
	}
	
	
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("Index: ").append(index).append("\n");
		sb.append("Size: ").append(size).append("\n");
		
		sb.append(index).append("->");
		
		sb = printNode(sb, (HeaderNode)this.rightNode);
		
		sb.append("\n");
		
		sb = printRightNode(sb, (HeaderNode)this.rightNode);
		
		return sb.toString();
	}

	
	private StringBuffer printRightNode(StringBuffer sb, HeaderNode rightNode) {
		
		if (rightNode == this) {
			return sb;
		}else {
			return this.printRightNode(sb, (HeaderNode)rightNode.rightNode);
		}
	}


	private StringBuffer printNode(StringBuffer sb, HeaderNode rightNode) {
		sb.append(rightNode.index).append("(").append(rightNode.size).append(")");
		if (rightNode == this) {
			return sb;
		}else {
			sb.append("->");
			return this.printNode(sb, (HeaderNode)rightNode.rightNode);
		}
	}

}
