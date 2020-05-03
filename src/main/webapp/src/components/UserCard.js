import * as React from "react";
import {Button, Card} from "react-bootstrap";
import UserInfo from "./UserInfo";
import {config} from "../Constants";
import axios from 'axios';

const USER_URL = config.url.API_URL + 'user/';
const OAUTH_URL = config.url.API_URL + 'security/oauth';

class UserCard extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            user: null
        }
    }

    componentDidMount() {
        axios.get(USER_URL)
            .then(res => {
                this.setState({user: res.data})
            })
            .catch(err => {
                console.log('USER:GET:ERROR:');
                console.log(err);
                this.setState({user: null})
            });
    }

    render() {
        return (
            <Card style={{width: '20rem'}}>
                <Card.Body>
                    <Card.Title>intuit.com user</Card.Title>
                    <UserInfo user={this.state.user}/>
                    <Button variant="primary" href={OAUTH_URL}
                            disabled={this.state.user !== null}>
                        OAuth with intuit.com
                    </Button>
                </Card.Body>
            </Card>
        );
    }
}

export default UserCard;
