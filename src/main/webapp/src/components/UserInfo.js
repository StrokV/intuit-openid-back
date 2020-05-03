import React from "react";

class UserInfo extends React.Component {

    render() {
        if (this.props.user !== null) {
            return (
                <dev>
                    <p>Name: {this.props.user.givenName}</p>
                    <p>Last name: {this.props.user.familyName}</p>
                    <p>Email: {this.props.user.email}</p>
                </dev>
            )
        }
        return (
            <div>
                <p>User is not authenticated.</p>
                <p>Please use OAuth.</p>
            </div>
        )
    }

}

export default UserInfo;
