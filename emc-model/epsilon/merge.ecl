@greedy
rule Components
match left : LEFT!Component
with right : RIGHT!Component {
	guard : true
	compare : left.name == right.name
}