rule Components
match left: LEFT!Component
with right: RIGHT!Component {
	compare: left.name == right.name
}

rule Events
match left: LEFT!Event
with right: RIGHT!Event {
	compare: left.name == right.name
}
