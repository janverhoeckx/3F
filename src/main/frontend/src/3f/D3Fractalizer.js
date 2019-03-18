import * as d3 from 'd3'

export class D3Fractalizer {

    constructor(data) {
        this.width = window.innerWidth - 50;
        this.height = window.innerHeight - 50;
        this.svg = d3.select("svg")
            .attr("width", this.width)
            .attr("height", this.height);


        console.debug("Using data: ", data);
        this.updateWithData(data);
        console.debug("fractializer initialized");
    }

    createForce(width, height) {
        return d3.layout.force()
            .linkDistance(140)
            .charge(-900)
            .gravity(.06)
            .size([width, height])
            .on("tick", () => this.transform());
    }

    transform() {
        this.links.attr("x1", (d) => {
            return d.source.x;
        })
            .attr("y1", (d) => {
                return d.source.y;
            })
            .attr("x2", (d) => {
                return d.target.x;
            })
            .attr("y2", (d) => {
                return d.target.y;
            });

        this.nodes.attr("transform", (d) => {
            return "translate(" + d.x + "," + d.y + ")";
        });
    }

    updateWithData(data){
        this.svg.selectAll(".link").remove();
        this.svg.selectAll(".node").remove();
        this.force = this.createForce(this.width, this.height);
        this.links = this.svg.selectAll(".link");
        this.nodes = this.svg.selectAll(".node");
        this.originalNodes = data;
        this.update(this.originalNodes);
    }

    update(rootNode) {
        let nodeModel = this.flatten(rootNode);
        let linkModel = d3.layout.tree().links(nodeModel);

        this.updateForce(linkModel, nodeModel);
        this.updateLinks(linkModel);
        this.updateNodes(nodeModel);
    }

    updateForce(linkModel, nodeModel) {
        this.force
            .nodes(nodeModel)
            .links(linkModel)
            .start();
    }

    updateLinks(linkModel) {
        this.links = this.links.data(linkModel, (data) => {
            return data.target.id;
        });
        this.links.enter().insert("line", ".node")
            .attr("class", "link");
        this.links.exit().remove();
    }

    updateNodes(nodeModel) {
        this.nodes = this.nodes.data(nodeModel, (data) => {
            return data.id;
        });
        this.createNodeItems();
        this.updateNodeStyles();

        this.nodes.exit().remove();
    }

    createNodeItems() {
        let nodeEnter = this.nodes.enter().append("g")
            .attr("class", (d) => this.styleClass(d))
            .on("click", (d) => this.selectNode(d))
            .on("dblclick", () => this.resetNodes())
            .call(this.force.drag);
        nodeEnter.append("circle");
        nodeEnter.append("text");
        nodeEnter.append("text");
    }

    updateNodeStyles() {
        this.nodes.select("circle")
            .attr("r", function (d) {
                return Math.sqrt(500000 / d.level) / 10 || 4.5;
            }).attr("class", (d) => this.styleClass(d));
        this.nodes.select("g text:first-of-type")
            .attr("dy", ".35em")
            .attr("class", (d) => this.nameStyleClass(d)).text(function (d) {
            return d.name;
        });
        this.nodes.select("g text:last-of-type")
            .attr("dy", ".35em")
            .attr("y", "20px")
            .attr("class", (d) => this.descriptionStyleClass(d)).text(function (d) {
            return d.description;
        });
    }


    styleClass(d) {
        return d.okay ? "node okay" : "node failed"
    }

    nameStyleClass(d) {
        return "name level" + d.level;
    }

    descriptionStyleClass(d) {
        return "description level" + d.level;
    }

    selectNode(d) {
        if (d3.event.defaultPrevented) return;

        this.update(d);
    }

    resetNodes() {
        if (d3.event.defaultPrevented) return;

        this.update(this.originalNodes);
    }

    flatten(root) {
        let nodes = [], i = 0;
        recurse(root, 1);
        return nodes;

        function recurse(node, level) {

            //FIXME when fitness function is defined call function for result


            if (node.children) node.children.forEach((theNode) => {
                recurse(theNode, level + 1);
            });

            //FIXME when this function is okay and all children are okay -> okay

            if (!node.id) node.id = ++i;
            node.level = level;
            nodes.push(node);
        }
    }
}
