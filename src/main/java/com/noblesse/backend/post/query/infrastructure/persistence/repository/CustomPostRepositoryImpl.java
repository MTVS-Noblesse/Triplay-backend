package com.noblesse.backend.post.query.infrastructure.persistence.repository;

import com.noblesse.backend.file.entity.QFile;
import com.noblesse.backend.oauth2.entity.QOAuthUser;
import com.noblesse.backend.post.common.entity.QPost;
import com.noblesse.backend.trip.domain.QTrip;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    public CustomPostRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public List<Tuple> findPostsWithDetails() {
        QPost post = QPost.post;
        QFile thumbnailFile = new QFile("thumbnailFile");
        QOAuthUser user = QOAuthUser.oAuthUser;
        QFile profileFile = new QFile("profileFile");
        QTrip trip = QTrip.trip;

        JPQLQuery<Long> minFileIdSubquery = JPAExpressions
                .select(thumbnailFile.fileId.min())
                .from(thumbnailFile)
                .where(thumbnailFile.postId.eq(post.postId)
                        .and(thumbnailFile.fileType.eq("post")));

        return queryFactory
                .select(post,
                        user.userName,
                        profileFile.fileUrl,
                        trip.tripStartDate,
                        trip.tripEndDate,
                        trip.tripParty,
                        thumbnailFile.fileUrl)
                .from(post)
                .leftJoin(user).on(post.userId.eq(user.id))
                .leftJoin(profileFile).on(user.profileId.eq(profileFile.fileId)
                        .and(profileFile.fileType.eq("profile")))
                .leftJoin(thumbnailFile).on(thumbnailFile.fileId.eq(minFileIdSubquery))
                .leftJoin(trip).on(post.tripId.eq(trip.tripId))
                .fetch();
    }

}
