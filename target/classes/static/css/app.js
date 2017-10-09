var File = React.createClass({

  getInitialState: function() {
    return {display: true };
  },
  handleDelete() {
    var self = this;
    $.ajax({
        url: self.props.file._links.self.href,
        type: 'DELETE',
        success: function(result) {
          self.setState({display: false});
        },
        error: function(xhr, ajaxOptions, thrownError) {
          toastr.error(xhr.responseJSON.message);
        }
    });
  },
  render: function() {

    if (this.state.display==false) return null;
    else return (
      <tr>
          <td>{this.props.file.fileName}</td>
          <td>{this.props.file.ReportResults}</td>
          <td>{this.props.file.time}</td>
          <td>
            <button className="btn btn-info" onClick={this.handleDelete}>Delete</button>
          </td>
      </tr>
    );
  }
});

var FileTable = React.createClass({

  render: function() {

    var rows = [];
    this.props.files.forEach(function(employee) {
      rows.push(
        <File file={file} key={file.fileName} />);
    });

    return (
      <table className="table table-striped">
          <thead>
              <tr>
                  <th>File Name</th>
                  <th>Report Results</th>
                  <th>Time</th>
                  <th>Delete</th>
              </tr>
          </thead>
          <tbody>{rows}</tbody>
      </table>
    );
  }
});

var App = React.createClass({

  loadFilesFromServer: function() {

    var self = this;
    $.ajax({
        url: "http://localhost:3306/fileModel",
      }).then(function(data) {
        self.setState({ files: data._embedded.files });
      });

  },

  getInitialState: function() {
    return { files: [] };
  },

  componentDidMount: function() {
    this.loadFilesFromServer();
  },

  render() {
    return ( <FileTable files={this.state.files} /> );
  }
});

ReactDOM.render(<App />, document.getElementById('root') );