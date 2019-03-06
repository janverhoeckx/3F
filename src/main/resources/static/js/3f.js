var width = window.innerWidth - 100;
var height = window.innerHeight - 100;
var currentMainNode;
var rootNode;
var link;
var node;
var svg;

var force = d3.layout.force()
    .linkDistance(80)
    .charge(-120)
    .gravity(.01)
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

    // Update links.
    link = link.data(links, function (d) {
        return d.target.id;
    });

    link.exit().remove();

    link.enter().insert("line", ".node")
        .attr("class", "link");

    // Update nodes.
    node = node.data(nodes, function (d) {
        return d.id;
    });

    node.exit().remove();

    var nodeEnter = node.enter().append("g")
        .attr("class", styleClass)
        .on("click", selectNode)
        .on("dblclick", resetNodes)
        .call(force.drag);


    nodeEnter.append("circle")
        .attr("r", function (d) {
            return Math.sqrt(d.size) / 10 || 4.5;
        });

    nodeEnter.append("text")
        .attr("dy", ".35em")
        .text(function (d) {
            return d.name;
        });
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

function styleClass(d)
{
    return d.okay ? "node okay" : "node failed"
}

function selectNode(d) {
    if (d3.event.defaultPrevented) return; // ignore drag

    currentMainNode = d;
    update();
}

function resetNodes() {
    if (d3.event.defaultPrevented) return; // ignore drag

    currentMainNode = rootNode;
    update();
}

function flatten(root) {
    var nodes = [], i = 0;

    function recurse(node, size) {
        if (node.children) node.children.forEach(function (theNode) {
            recurse(theNode, size / 2);
        });

        if (!node.id) node.id = ++i;
        node.size = size;
        nodes.push(node);
    }

    recurse(root, 220000);
    return nodes;
}