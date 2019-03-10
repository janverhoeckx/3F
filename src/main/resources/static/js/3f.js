var originalNodes;
var force;
var links;
var nodes;

function show3F(jsonPath) {
    var width = window.innerWidth - 50;
    var height = window.innerHeight - 50;
    var svg = d3.select("svg")
        .attr("width", width)
        .attr("height", height);

    createForce(width, height);

    links = svg.selectAll(".link");
    nodes = svg.selectAll(".node");

    readJSONAndUpdate(jsonPath);
}

function createForce(width, height) {
    force = d3.layout.force()
        .linkDistance(140)
        .charge(-900)
        .gravity(.06)
        .size([width, height])
        .on("tick", transform);
}

function transform() {
    links.attr("x1", function (d) {
        return d.source.x;
    })
        .attr("y1", function (d) {
            return d.source.y;
        })
        .attr("x2", function (d) {
            return d.target.x;
        })
        .attr("y2", function (d) {
            return d.target.y;
        });

    nodes.attr("transform", function (d) {
        return "translate(" + d.x + "," + d.y + ")";
    });
}

function readJSONAndUpdate(jsonPath) {
    d3.json(jsonPath, function (error, json) {
        if (error) throw error;

        originalNodes = json;
        update(originalNodes);
    });
}

function update(rootNode) {

    var nodeModel = flatten(rootNode);
    var linkModel = d3.layout.tree().links(nodeModel);

    updateForce(linkModel, nodeModel);
    updateLinks(linkModel);
    updateNodes(nodeModel);

}

function updateForce(linkModel, nodeModel) {
    force
        .nodes(nodeModel)
        .links(linkModel)
        .start();
}

function updateLinks(linkModel) {
    links = links.data(linkModel, function (d) {
        return d.target.id;
    });
    links.enter().insert("line", ".node")
        .attr("class", "link");
    links.exit().remove();
}

function updateNodes(nodeModel) {
    nodes = nodes.data(nodeModel, function (d) {
        return d.id;
    });
    createNodeItems();
    updateNodeStyles();

    nodes.exit().remove();
}

function createNodeItems() {
    var nodeEnter = nodes.enter().append("g")
        .attr("class", styleClass)
        .on("click", selectNode)
        .on("dblclick", resetNodes)
        .call(force.drag);
    nodeEnter.append("circle");
    nodeEnter.append("text");
    nodeEnter.append("text");
}

function updateNodeStyles() {
    nodes.select("circle")
        .attr("r", function (d) {
            return Math.sqrt(500000 / d.level) / 10 || 4.5;
        }).attr("class", styleClass);
    nodes.select("g text:first-of-type")
        .attr("dy", ".35em")
        .attr("class", nameStyleClass).text(function (d) {
        return d.name;
    });
    nodes.select("g text:last-of-type")
        .attr("dy", ".35em")
        .attr("y", "20px")
        .attr("class", descriptionStyleClass).text(function (d) {
        return d.description;
    });
}


function styleClass(d) {
    return d.okay ? "node okay" : "node failed"
}

function nameStyleClass(d) {
    return "name level" + d.level;
}

function descriptionStyleClass(d) {
    return "description level" + d.level;
}

function selectNode(d) {
    if (d3.event.defaultPrevented) return;

    update(d);
}

function resetNodes() {
    if (d3.event.defaultPrevented) return;

    update(originalNodes);
}

function flatten(root) {
    var nodes = [], i = 0;
    recurse(root, 1);
    return nodes;

    function recurse(node, level) {

        //FIXME when fitness function is defined call function for result


        if (node.children) node.children.forEach(function (theNode) {
            recurse(theNode, level + 1);
        });

        //FIXME when this function is okay and all children are okay -> okay

        if (!node.id) node.id = ++i;
        node.level = level;
        nodes.push(node);
    }
}