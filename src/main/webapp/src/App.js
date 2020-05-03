import React from 'react';
import {Container, Row, Col} from "react-bootstrap";
import UserCard from "./components/UserCard";

function App() {
    return (
        <Container>
            <Row className="justify-content-md-center">
                <Col md="auto">
                    <h1>An example of OIDC integration with intuit.com</h1>
                </Col>
            </Row>
            <hr/>
            <Row>
                <Col className="left-container">
                    <UserCard/>
                </Col>
            </Row>
        </Container>
    );
}

export default App;
