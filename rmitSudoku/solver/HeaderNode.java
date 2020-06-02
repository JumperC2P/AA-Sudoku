package solver;

import java.util.List;

/**
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
	
	public List<DLXNode> coverNodesWithSkipped(List<DLXNode> coverCageNodes, DLXNode skipNode) {
		return this.coverDownNodesWithSkipped(coverCageNodes, skipNode, this.downNode);
	}
	
	private List<DLXNode> coverDownNodesWithSkipped(List<DLXNode> coverCageNodes, DLXNode skipNode, DLXNode downNode) {
		
		if (downNode == this.headerNode) {
			return coverCageNodes;
		}else if (downNode == skipNode){
			return this.coverDownNodesWithSkipped(coverCageNodes, skipNode, downNode.downNode);
		}else {
			downNode.coverUD();
			if (downNode != downNode.headerNode)
				downNode.headerNode.size--;
			coverCageNodes.add(downNode);
			this.coverRightNode(downNode, downNode.rightNode);
			return this.coverDownNodesWithSkipped(coverCageNodes, skipNode, downNode.downNode);
		}
	}
	

	public void uncoverNodesOnNode(DLXNode baseNode) {
		this.uncoverUpNodesOnNode(baseNode, baseNode.downNode);
	}

	private void uncoverUpNodesOnNode(DLXNode baseNode, DLXNode endNode) {
		if (baseNode == endNode) {
			return;
		}else {
			baseNode.uncoverUD();
			if (baseNode != baseNode.headerNode)
				baseNode.headerNode.size++;
			this.uncoverLeftNode(baseNode, baseNode.leftNode);
//			this.uncoverUpNodesOnNode(baseNode.upNode, endNode);
		}
		
	}

	
	public void coverNodes() {
		
		// cover "column" which is header column itself
		this.coverLR();
		// cover the nodes which connect with the header
		this.coverDownNodes(this.downNode);
	}

	private void coverDownNodes(DLXNode downNode) {
		
		if (downNode == this.headerNode) {
			return;
		}else {
			this.coverRightNode(downNode, downNode.rightNode);
			this.coverDownNodes(downNode.downNode);
		}
	}
	
	private void coverRightNode(DLXNode downNode, DLXNode rightNode) {
		if (rightNode == downNode) {
			return;
		}else {
			rightNode.coverUD();
			rightNode.headerNode.size--;
			this.coverRightNode(downNode, rightNode.rightNode);
		}
	}

	public void uncoverNodes() {
		this.uncoverUpNodes(this.upNode);
		this.uncoverLR();
	}

	private void uncoverUpNodes(DLXNode upNode) {
		if (upNode == this.headerNode) {
			return;
		}else {
			this.uncoverLeftNode(upNode, upNode.leftNode);
			this.uncoverUpNodes(upNode.upNode);
		}
		
	}

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
//			sb.append(rightNode.index).append("(").append(rightNode.size).append("): ");
//			sb = this.printDownNode(sb, rightNode, rightNode.downNode);
//			sb.append("\n");
			return this.printRightNode(sb, (HeaderNode)rightNode.rightNode);
		}
	}

	private StringBuffer printDownNode(StringBuffer sb, HeaderNode headerNode, DLXNode downNode) {
		
		sb.append(downNode.value);
		if (downNode == headerNode) {
			return sb;
		}else {
			sb.append("->");
			return this.printDownNode(sb, headerNode, downNode.downNode);
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
