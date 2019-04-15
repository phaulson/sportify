/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Martin
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    /**
     *
     * @return
     */
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(services.AddCommentResource.class);
        resources.add(services.AddDailyWorkoutResource.class);
        resources.add(services.AddExerciseResource.class);
        resources.add(services.AddLocationResource.class);
        resources.add(services.AddPlanResource.class);
        resources.add(services.AddPostResource.class);
        resources.add(services.AddWorkoutResource.class);
        resources.add(services.ChangeDescriptionResource.class);
        resources.add(services.GetCommentsResource.class);
        resources.add(services.GetDailyWorkoutsResource.class);
        resources.add(services.GetExerciseResource.class);
        resources.add(services.GetExercisesResource.class);
        resources.add(services.GetFollowedUsersCountResource.class);
        resources.add(services.GetFollowedUsersResource.class);
        resources.add(services.GetFollowersCountResource.class);
        resources.add(services.GetFollowersResource.class);
        resources.add(services.GetLocationsResource.class);
        resources.add(services.GetNearbyLocationsResource.class);
        resources.add(services.GetNumberOfLikesResource.class);
        resources.add(services.GetPlansResource.class);
        resources.add(services.GetPostsByCreatorResource.class);
        resources.add(services.GetPostsResource.class);
        resources.add(services.GetProfileResource.class);
        resources.add(services.GetSubscribedPlansResource.class);
        resources.add(services.GetSubscribersCountResource.class);
        resources.add(services.GetWorkoutsResource.class);
        resources.add(services.IsFollowingResource.class);
        resources.add(services.IsLikedResource.class);
        resources.add(services.IsPlanSubscribedResource.class);
        resources.add(services.IsProResource.class);
        resources.add(services.LinkDailyWorkoutsResource.class);
        resources.add(services.LinkExercisesResource.class);
        resources.add(services.LinkWorkoutsResource.class);
        resources.add(services.LoginResource.class);
        resources.add(services.RegisterResource.class);
        resources.add(services.SearchDailyWorkoutsResource.class);
        resources.add(services.SearchExercisesResource.class);
        resources.add(services.SearchPlansResource.class);
        resources.add(services.SearchUsersResource.class);
        resources.add(services.SearchWorkoutsResource.class);
        resources.add(services.SetLikeResource.class);
        resources.add(services.SetPlanSubscriptionResource.class);
        resources.add(services.SetUserFollowResource.class);
    }
    
}
