import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert, Button } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9">
        {account?.login ? (
          <div>
            <h2>
              Hello, {account.login}.
            </h2>
            <p className="lead">
               May your skin bloom :)
            </p>
            <p>
              <Button tag={Link} to="/cabinet">Cabinet</Button>
            </p>
            <p>
              <Button tag={Link} to="/product-directory">Product Diretory</Button>
            </p>
          </div>
        ) : (
          <div>
            <h2>
              Welcome to Blooming Days Skin Care Tracker
            </h2>
            <Alert color="warning">
              <Translate contentKey="global.messages.info.authenticated.prefix">If you want to </Translate>

              <Link to="/login" className="alert-link">
                <Translate contentKey="global.messages.info.authenticated.link"> sign in</Translate>
              </Link>
              <Translate contentKey="global.messages.info.authenticated.suffix">
                , you can try the default accounts:
                <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;)
                <br />- User (login=&quot;user&quot; and password=&quot;user&quot;).
              </Translate>
            </Alert>

            <Alert color="warning">
              <Translate contentKey="global.messages.info.register.noaccount">You do not have an account yet?</Translate>&nbsp;
              <Link to="/account/register" className="alert-link">
                <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
              </Link>
            </Alert>
          </div>
        )}
      </Col>
    </Row>
  );
};

export default Home;

/*

  <Alert color="success">
    <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
      You are logged in as user {account.login}.
    </Translate>
  </Alert>

     ^^insert line 30 if need
*/
