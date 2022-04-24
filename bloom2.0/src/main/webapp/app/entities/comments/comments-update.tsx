import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IProductFeed } from 'app/shared/model/product-feed.model';
import { getEntities as getProductFeeds } from 'app/entities/product-feed/product-feed.reducer';
import { IComments } from 'app/shared/model/comments.model';
import { getEntity, updateEntity, createEntity, reset } from './comments.reducer';

export const CommentsUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const productFeeds = useAppSelector(state => state.productFeed.entities);
  const commentsEntity = useAppSelector(state => state.comments.entity);
  const loading = useAppSelector(state => state.comments.loading);
  const updating = useAppSelector(state => state.comments.updating);
  const updateSuccess = useAppSelector(state => state.comments.updateSuccess);
  const handleClose = () => {
    props.history.push('/comments');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
    dispatch(getProductFeeds({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...commentsEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
      productFeed: productFeeds.find(it => it.id.toString() === values.productFeed.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...commentsEntity,
          user: commentsEntity?.user?.id,
          productFeed: commentsEntity?.productFeed?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bloomApp.comments.home.createOrEditLabel" data-cy="CommentsCreateUpdateHeading">
            <Translate contentKey="bloomApp.comments.home.createOrEditLabel">Create or edit a Comments</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="comments-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bloomApp.comments.product')}
                id="comments-product"
                name="product"
                data-cy="product"
                type="text"
              />
              <ValidatedField label={translate('bloomApp.comments.date')} id="comments-date" name="date" data-cy="date" type="date" />
              <ValidatedField
                label={translate('bloomApp.comments.comment')}
                id="comments-comment"
                name="comment"
                data-cy="comment"
                type="text"
                validate={{
                  maxLength: { value: 128, message: translate('entity.validation.maxlength', { max: 128 }) },
                }}
              />
              <ValidatedBlobField
                label={translate('bloomApp.comments.image')}
                id="comments-image"
                name="image"
                data-cy="image"
                isImage
                accept="image/*"
              />
              <ValidatedField id="comments-user" name="user" data-cy="user" label={translate('bloomApp.comments.user')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="comments-productFeed"
                name="productFeed"
                data-cy="productFeed"
                label={translate('bloomApp.comments.productFeed')}
                type="select"
              >
                <option value="" key="0" />
                {productFeeds
                  ? productFeeds.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/comments" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CommentsUpdate;
