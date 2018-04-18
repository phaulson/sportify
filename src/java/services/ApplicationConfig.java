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
        resources.add(services.AddLocationResource.class);
        resources.add(services.ChangeDescriptionResource.class);
        resources.add(services.GetDailyWorkoutsResource.class);
        resources.add(services.GetExercisesResource.class);
        resources.add(services.GetLocationsResource.class);
        resources.add(services.GetPlansResource.class);
        resources.add(services.GetPostsByCreatorResource.class);
        resources.add(services.GetProfileResource.class);
        resources.add(services.GetWorkoutsResource.class);
        resources.add(services.LoginResource.class);
        resources.add(services.RegisterResource.class);
    }
    
}
