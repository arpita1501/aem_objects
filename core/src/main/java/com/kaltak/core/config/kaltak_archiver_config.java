package com.kaltak.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "Kaltak Article Archiver Configuration",
        description = "Configuration for automatic archiving old articles"
)
public @interface kaltak_archiver_config {

    @AttributeDefinition(
            name = "Cron Expression",
            description = "Runs every day at 3 AM"
    )
    String scheduler_expression() default "0 0 3 * * ?";

    @AttributeDefinition(
            name = "News Root Path"
    )
    String news_root() default "/content/kaltak/en/news";

    @AttributeDefinition(
            name = "Archive Root Path"
    )
    String archive_root() default "/content/kaltak/en/archive";

    @AttributeDefinition(
            name = "Archive After Days"
    )
    int days_limit() default 365;

    @AttributeDefinition(
            name = "Batch Size"
    )
    int batch_size() default 20;
}