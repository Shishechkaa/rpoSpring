import React from 'react';
import {Nav, Navbar} from 'react-bootstrap'
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import {faHome, faUser, faBars} from '@fortawesome/free-solid-svg-icons'
import {Link, useNavigate} from 'react-router-dom';
import Utils from "../utils/Utils";
import BackendService from "../services/BackendService";
import {connect, useDispatch} from "react-redux";
import {userActions} from "../utils/Rdx";

const NavigationBar = (props) => {
    let navigate = useNavigate();
    let uname = Utils.getUserName();
    const dispatch = useDispatch();

    const goHome = () => {
        navigate("home", { replace: true });
    }

    const logout = () => {
        BackendService.logout()
            .then(() => {
                Utils.removeUser();
                dispatch(userActions.logout());
                navigate("login", { replace: true });
            })
    }

    return (
        <Navbar bg="light" expand="lg">
            <button type="button"
                    className="btn btn-outline-secondary mr-2"
                    onClick={props.toggleSideBar}>
                <FontAwesomeIcon icon={faBars} />
            </button>
            <Navbar.Brand><FontAwesomeIcon icon={faHome} />{' '}My RPO</Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="me-auto">
                    {/*<Nav.Link href={"\home"}>Home</Nav.Link>*/}
                    <Nav.Link as={Link} to="/home">Home</Nav.Link>
                    <Nav.Link onClick={goHome}>Another Home</Nav.Link>
                    <Nav.Link onClick={goHome}>Yet Another Home</Nav.Link>
                </Nav>
                <Navbar.Text>{uname}</Navbar.Text>
                { props.user &&
                    <Nav.Link onClick={logout}><FontAwesomeIcon icon={faUser} fixedWidth />{' '}Выход</Nav.Link>
                }
                { !props.user &&
                    <Nav.Link as={Link} to="/login"><FontAwesomeIcon icon={faUser} fixedWidth />{' '}Вход</Nav.Link>
                }
            </Navbar.Collapse>
        </Navbar>
    );
};

const mapStateToProps = state => {
    const { user } = state.authentication;
    return { user };
}
export default connect(mapStateToProps)(NavigationBar);
