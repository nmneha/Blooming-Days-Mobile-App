import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './comments.reducer';

export const CommentsDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const commentsEntity = useAppSelector(state => state.comments.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="commentsDetailsHeading">
          <Translate contentKey="bloomApp.comments.detail.title">Comments</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{commentsEntity.id}</dd>
          <dt>
            <span id="product">
              <Translate contentKey="bloomApp.comments.product">Product</Translate>
            </span>
          </dt>
          <dd>{commentsEntity.product}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="bloomApp.comments.date">Date</Translate>
            </span>
          </dt>
          <dd>{commentsEntity.date ? <TextFormat value={commentsEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="comment">
              <Translate contentKey="bloomApp.comments.comment">Comment</Translate>
            </span>
          </dt>
          <dd>{commentsEntity.comment}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="bloomApp.comments.image">Image</Translate>
            </span>
          </dt>
          <dd>
            {commentsEntity.image ? (
              <div>
                {commentsEntity.imageContentType ? (
                  <a onClick={openFile(commentsEntity.imageContentType, commentsEntity.image)}>
                    <img src={`data:${commentsEntity.imageContentType};base64,${commentsEntity.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {commentsEntity.imageContentType}, {byteSize(commentsEntity.image)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="bloomApp.comments.productFeed">Product Feed</Translate>
          </dt>
          <dd>{commentsEntity.productFeed ? commentsEntity.productFeed.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/comments" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/comments/${commentsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CommentsDetail;
