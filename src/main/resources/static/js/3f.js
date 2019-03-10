var width = window.innerWidth - 100;
var height = window.innerHeight - 100;
var currentMainNode;
var rootNode;
var link;
var node;
var svg;

//FIXME linkDistance depending on level
var force = d3.layout.force()
    .linkDistance(130)
    .charge(-450)
    .gravity(.06)
    .size([width, height])
    .on("tick", tick);


function show(d3Svg, jsonPath) {
    svg = d3Svg;
    link = svg.selectAll(".link");
    node = svg.selectAll(".node");


    d3.json(jsonPath, function (error, json) {
        if (error) throw error;

        rootNode = json;
        currentMainNode = rootNode;

        update();
    });
}

function update() {

    var nodes = flatten(currentMainNode);
    var links = d3.layout.tree().links(nodes);

    // Restart the force layout.
    force
        .nodes(nodes)
        .links(links)
        .start();

    link = link.data(links, function (d) {
        return d.target.id;
    });
    link.enter().insert("line", ".node")
        .attr("class", "link");
    link.exit().remove();


    node = node.data(nodes, function (d) {
        return d.id;
    });

    var nodeEnter = node.enter().append("g")
        .attr("class", styleClass)
        .on("click", selectNode)
        .on("dblclick", resetNodes)
        .call(force.drag);
    nodeEnter.append("circle");
    nodeEnter.append("text");
    nodeEnter.append("text");

    node.select("circle")
        .attr("r", function (d) {
            return Math.sqrt(500000 / d.level) / 10 || 4.5;
        }).attr("class", styleClass);
    node.select("g text:first-of-type")
        .attr("dy", ".35em")
        .attr("class", nameStyleClass).text(function (d) {
        return d.name;
    });
    node.select("g text:last-of-type")
        .attr("dy", ".35em")
        .attr("y", "20px")
        .attr("class", descriptionStyleClass).text(function (d) {
        return d.description;
    });

    node.exit().remove();

}

function tick() {
    link.attr("x1", function (d) {
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

    node.attr("transform", function (d) {
        return "translate(" + d.x + "," + d.y + ")";
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

    currentMainNode = d;
    update();
}

function resetNodes() {
    if (d3.event.defaultPrevented) return;

    currentMainNode = rootNode;
    update();
}

function flatten(root) {
    var nodes = [], i = 0;

    function recurse(node, level) {


        if (node.children) node.children.forEach(function (theNode) {
            recurse(theNode, level + 1);
        });

        if (!node.id) node.id = ++i;

        node.level = level;
        nodes.push(node);
    }


    recurse(root, 1);
    return nodes;
}