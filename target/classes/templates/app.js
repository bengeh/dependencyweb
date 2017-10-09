const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

class App extends React.Component {

	constructor(props) {
	    console.log("inside the js #$#$@#$@");
		super(props);
		this.state = {files: []};
	}

	componentDidMount() {
	    console.log("inside the mounted: 3$@#$@#$");
		client({method: 'GET', path: '/scanned'}).done(response => {
			this.setState({files: response.entity._embedded.files});
		});
	}

	render() {
		return (
			<FileList files={this.state.files}/>
		)
	}
}

class FileList extends React.Component{
	render() {
	    console.log("nihaoma");
		var files = this.props.files.map(file =>
			<File key={file._links.self.href} file={file}/>
		);
		return (
			<table>
				<tbody>
					<tr>
						<th>File Name</th>
						<th>Report Results</th>
						<th>Time</th>
					</tr>
					{files}
				</tbody>
			</table>
		)
	}
}

class File extends React.Component{
	render() {
	    console.log("zaijianle $#@#$@");
		return (
			<tr>
				<td>{this.props.file.fileName}</td>
				<td>{this.props.file.ReportResults}</td>
				<td>{this.props.file.time}</td>
			</tr>
		)
	}
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
)